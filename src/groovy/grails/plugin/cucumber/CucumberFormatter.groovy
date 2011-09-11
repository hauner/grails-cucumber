/*
 * Copyright 2011 Martin Hauner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.cucumber

import gherkin.formatter.Reporter
import gherkin.formatter.Formatter
import gherkin.formatter.PrettyFormatter
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.Step
import gherkin.formatter.model.Background
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Result
import gherkin.formatter.model.Match

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import junit.framework.AssertionFailedError


class CucumberFormatter2 implements Formatter, Reporter {
    GrailsTestEventPublisher publisher
    FeatureReport report
    Formatter formatter
    Reporter reporter

    Feature activeFeature
    Scenario activeScenario
    Step activeStep

    int runCount
    int failureCount
    int errorCount

    def sysout

    CucumberFormatter2 (GrailsTestEventPublisher publisher, FeatureReport report,
        Formatter formatter, Reporter reporter) {
        this.publisher = publisher
        this.formatter = formatter
        this.reporter = reporter
        this.report = report

        sysout = System.out
    }

    void finish () {
        sysout << "CF(finish)\n"

        //report.endScenario ()
        report.endFeature ()
        publisher.testEnd (activeScenario.getName ())

//        report.endFeature ()
        publisher.testCaseEnd (activeFeature.getName ())
    }

    CucumberTestTypeResult getResult () {
        new CucumberTestTypeResult (runCount, failureCount, errorCount)
    }
    
    /*
     * Formatter
     */
    void uri (String uri) {
        formatter.uri (uri)
    }

    void feature (Feature feature) {
        if (activeFeature) {
            sysout << "CF(feature end)\n"

            report.endFeature ()
            publisher.testCaseEnd (activeFeature.getName ())
        }

        sysout << "CF(feature start)\n"
        activeFeature = feature
        publisher.testCaseStart (activeFeature.getName ())
        report.startFeature (activeFeature.getName ())

        formatter.feature (feature)
    }

    void background (Background background) {
        formatter.background (background)
    }

    void scenario (Scenario scenario) {
        if (activeScenario) {
            sysout << "CF(scenario end)\n"

            report.endScenario ()
            publisher.testEnd (activeScenario.getName ())
        }

        sysout << "CF(scenario start)\n"
        activeScenario = scenario
        publisher.testStart (activeScenario.getName ())
        report.startScenario (activeScenario.getName ())

        formatter.scenario (scenario)

        runCount++
    }

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        formatter.scenarioOutline (scenarioOutline)
    }

    void examples (Examples examples) {
        formatter.examples (examples)
    }

    void step (Step step) {
        sysout << "CF(step)\n"

        activeStep = step

        formatter.step (step)
    }

    void eof () {
        formatter.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, int line) {
        formatter.syntaxError (state, event, legalEvents, uri, line)
    }

    /*
     * Reporter
     */

    // TODO test happy path
    void result (Result result) {
        sysout << "CF(result)\n"

        if (result.getErrorMessage () != null) {
            
            if (result.error instanceof AssertionFailedError) {
                report.addFailure ((AssertionFailedError)result.error)
                publisher.testFailure (activeStep.getName (), result.error)

                failureCount++
            }
            else {
                report.addError (error)
                publisher.testFailure (activeStep.getName (), result.error, true)

                errorCount++
            }
        }

        reporter.result (result)
    }

    void match (Match match) {
        reporter.match (match)
    }

    void embedding (String s, byte[] bytes) {
        reporter.embedding (s, bytes)
    }

}




class CucumberFormatter implements Formatter, Reporter {
    GrailsTestEventPublisher eventPublisher
    PrettyFormatterFactory formatterFactory
    JUnitReportsFactory reportsFactory
    SystemOutAndErrSwapper outAndErrSwapper

    def sysout



    Formatter formatter
    Feature activeFeature = null
    Scenario activeScenario = null
    Step activeStep = null

    /*TestSuite*/
    def testSuite
    def test
    /*JUnitReports*/
    def reports
    def startTime
    def uri

    def runCount = 0
    def failureCount = 0
    def errorCount = 0

    CucumberFormatter (
        GrailsTestEventPublisher eventPublisher,
        PrettyFormatterFactory formatterFactory,
        JUnitReportsFactory reportsFactory,
        SystemOutAndErrSwapper outAndErrSwapper) {
        this.formatterFactory = formatterFactory
        this.eventPublisher = eventPublisher
        this.reportsFactory = reportsFactory
        this.outAndErrSwapper = outAndErrSwapper
        this.sysout = System.out
    }

    CucumberTestTypeResult getResult () {
        new CucumberTestTypeResult (runCount, failureCount, errorCount)
    }

    void uri (String uri) {
        sysout << "GF(uri): ($uri)\n"
        this.uri = uri
    }

    private void startFeature () {
        sysout << "GF(startFeature): (${activeFeature.getName ()})\n"

        eventPublisher.testCaseStart (activeFeature.getName ())

        ///
        outAndErrSwapper.swapIn ()
        reports = reportsFactory.createReports (activeFeature.getName ())
        testSuite = new JUnitTest (activeFeature.getName ())
        reports.startTestSuite (testSuite)
        startTime = System.currentTimeMillis ()

        //def pretty = new PrettyFormatter (sysout/*System.out*/, true, true)
        formatter = new PrettyFormatter (System.out, true, true)
        //formatter = new DebugFormatter (sysout, pretty)


        ///
    }

    private void finishFeature () {
        sysout << "GF(finishFeature): (${activeFeature.getName ()})\n"

        //
        testSuite.runTime = System.currentTimeMillis () - startTime

        testSuite.setCounts (runCount, failureCount, errorCount)

        def (out, err) = outAndErrSwapper.swapOut ()*.toString ()
        reports.systemOutput = out
        reports.systemError = err
        reports.endTestSuite (testSuite)
        //

        eventPublisher.testCaseEnd (activeFeature.getName ())
        sysout << "Counts: $runCount, $failureCount, $errorCount\n"
    }

    void finish () {
        finishScenario ()
        finishFeature ()
    }

    void feature (Feature feature) {
        //sysout << "GF(feature): (${feature.getName ()})\n"

        // finish previous feature
        if (activeFeature != null) {
            finishFeature ()
        }

        // start next feature
        activeFeature = feature
        startFeature ()

        // normal cucumber output
        formatter.uri (uri)
        formatter.feature (feature)
    }

    void background (Background background) {
        sysout << "GF(background): ($background)\n"

        formatter.background (background)
    }

    private void startScenario () {
        sysout << "\nGF(startScenario): (${activeScenario.getName ()})\n"

        eventPublisher.testStart (activeScenario.getName ())

        runCount++
        [System.out, System.err]*.println ("\n--Output from ${activeScenario.getName ()}--")

        test = new CucumberTest ()
        reports.startTest (test)
    }

    private void finishScenario () {
        sysout << "GF(finishScenario): (${activeScenario.getName ()})\n"

        reports.endTest (test)
        eventPublisher.testEnd (activeScenario.getName ())
    }

    void scenario (Scenario scenario) {

        if (activeScenario != null) {
            finishScenario ()
        }

        activeScenario = scenario
        startScenario ()

        formatter.scenario (scenario)
    }
    //eventPublisher.testFailure ()

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        sysout << "GF(scenarioOutline): (${scenarioOutline.getName ()}})\n"

        formatter.scenarioOutline (scenarioOutline)
    }

    void examples (Examples examples) {
        sysout << "GF(examples): (${examples.getName ()}})\n"

        formatter.examples (examples)
    }

    void step (Step step) {
        sysout << "GF(step): (${step.getName ()}})\n"

        activeStep = step
        formatter.step (step)
    }

    void eof () {
        sysout << "GF(eof)\n"

        formatter.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, int line) {
        sysout << "GF(syntaxError)\n"

        formatter.syntaxError (state, event, legalEvents, uri, line)
    }

    /*
    * Reporter
    */

    // for each step
    void result (Result result) {
        sysout << "GR(result):\n"
        //sysout << result.getStatus() + "\n"

        if (result.getErrorMessage () != null) {
            def exception = result.getError ()

            sysout << exception
            if (exception instanceof AssertionFailedError) {
                sysout << "GR(result): failure\n"

                failureCount++
                reports.addFailure (test, exception)

                eventPublisher.testFailure (activeStep.getName (), exception)
            }
            else {
                sysout << "GR(result): error\n"

                errorCount++
                reports.addError (test, exception)

                eventPublisher.testFailure (activeStep.getName (), exception, true)
            }
        }
        else {
            sysout << "GR(result): success\n"
            //????
            //reports.endTest (test)
            //eventPublisher.testEnd (activeScenario.getName ())
        }

        formatter.result (result)
    }

    void match (Match match) {
        sysout << "GR(match):\n"

        formatter.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        sysout << "GR(embedding):\n" //+ mimeType +" / "+ data
    }
}
