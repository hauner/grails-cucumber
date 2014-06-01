/*
 * Copyright 2011-2013 Martin Hauner
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
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.Step
import gherkin.formatter.model.Background
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Result
import gherkin.formatter.model.Match

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import cucumber.runtime.UndefinedStepException


class CucumberFormatter implements Formatter, Reporter {
    public static final String RESULT_PENDING = "pending"
    GrailsTestEventPublisher publisher
    FeatureReport report
    Formatter formatter
    Reporter reporter

    Feature activeFeature
    Scenario activeScenario
    Step activeStep
    List<Step> steps = []
    
    int runCount
    Set<String> failed    = new HashSet<String> ()
    Set<String> erroneous = new HashSet<String> ()

    def sysout

    Match beforeMatch
    Result beforeResult

    CucumberFormatter (GrailsTestEventPublisher publisher, FeatureReport report,
        Formatter formatter, Reporter reporter) {
        this.publisher = publisher
        this.formatter = formatter
        this.reporter = reporter
        this.report = report

        sysout = System.out
    }

    CucumberTestTypeResult getResult () {
        new CucumberTestTypeResult (runCount, failed.size (), erroneous.size ())
    }
    
    /*
     * Formatter
     */
    void uri (String uri) {
        formatter.uri (uri)
    }

    void feature (Feature feature) {
        endScenario ()
        endFeature ()

        startFeature (feature)

        formatter.feature (feature)
    }

    void background (Background background) {
        formatter.background (background)
    }

    void scenario (Scenario scenario) {
        endScenario ()

        startScenario (scenario)

        formatter.scenario (scenario)
        runCount++
    }

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        formatter.scenarioOutline (scenarioOutline)
    }

    void examples (Examples examples) {
        formatter.examples (examples)
    }

    @Override
    void startOfScenarioLifeCycle (Scenario scenario) {
        formatter.startOfScenarioLifeCycle (scenario)
    }

    void step (Step step) {
        steps.add (step)
        formatter.step (step)
    }

    @Override
    void endOfScenarioLifeCycle (Scenario scenario) {
        formatter.endOfScenarioLifeCycle (scenario)
    }

    void eof () {
        formatter.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, Integer line) {
        formatter.syntaxError (state, event, legalEvents, uri, line)
    }

    void done () {
        log.trace ("done ()\n")

        endScenario ()
        endFeature ()

        formatter.done ()
    }

    void close () {
        formatter.close ()
    }

    private void startFeature (Feature feature) {
        log.trace ("feature (${feature.name})\n")

        activeFeature = feature
        publisher.testCaseStart (activeFeature.name)
        report.startFeature (activeFeature.name)
    }

    private void startScenario (Scenario scenario) {
        log.trace ("  scenario (${scenario.name})\n")

        activeScenario = scenario
        publisher.testStart (activeScenario.name)
        report.startScenario (activeScenario.name)
    }

    private void endScenario () {
        if (activeScenario) {
            log.trace ("  end scenario ()\n")

            report.endScenario ()
            publisher.testEnd (activeScenario.name)
            activeScenario = null
        }
    }

    private void endFeature () {
        if (activeFeature) {
            log.trace ("end feature ()\n")

            report.endFeature ()
            publisher.testCaseEnd (activeFeature.name)
        }
    }

    /*
     * Reporter
     */

    void match (Match match) {
        reporter.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        reporter.embedding (mimeType, data)
    }

    void write (String s) {
        reporter.write (s)
    }

    void before (Match match, Result result) {
        log.trace ("  before (...)\n")

        beforeMatch = match
        beforeResult = result
        reporter.before (match, result)
    }

    void result (Result result) {
        log.trace ("      result (...)\n")

        advanceActiveStep ()

        if (result.status == Result.FAILED) {
            resultFailed (result.error)
        }
        else if (result == Result.SKIPPED) {
            resultSkipped (beforeResult?.error)
            beforeResult = null
            beforeMatch = null
        }
        else if (result == Result.UNDEFINED) {
            resultUndefined ()
        }
        else if (result.status == Result.PASSED) {
            resultPassed ()
        }
        else if (result.status == RESULT_PENDING) {
            resultFailed (result.error)
        }
        else {
            log.trace ("      result (${result.status}) is not handled!\n")
        }

        reporter.result (result)
    }

    void after (Match match, Result result) {
        reporter.after (match, result)
    }

    private void resultFailed (Throwable error) {
        if (error instanceof AssertionError) {
            failScenario (error)
        }
        else {
            errorScenario (error)
        }
    }

    private void resultSkipped (Throwable error) {
        if (error) {
            skipScenario (error)
        }
    }

    private void resultUndefined () {
        undefScenario (new UndefinedStepException (activeStep))
    }

    private void resultPassed () {
        report.addPassed ()
    }

    private void failScenario (AssertionError error) {
        report.addFailure (error)
        publisher.testFailure (activeScenarioName, error)
        countFailure ()
    }

    private void errorScenario (Throwable error) {
        report.addError (error)
        publisher.testFailure (activeScenarioName, error, true)
        countError ()
    }

    private void skipScenario (Throwable error) {
        report.addSkipped (error)
        publisher.testFailure (activeScenarioName, error)
        countFailure ()
    }

    private void undefScenario (Throwable error) {
        report.addUndefined (error)
        publisher.testFailure (activeScenarioName, error)
        countFailure ()
    }

    private void countFailure () {
        if (erroneous.count (activeScenarioName) == 0) {
            failed.add (activeScenarioName)
        }
    }

    private void countError () {
        if (failed.count (activeScenarioName) == 0) {
            erroneous.add (activeScenarioName)
        }
    }

    private void advanceActiveStep () {
        if (steps.size() > 0) {
            activeStep = steps.head ()
            steps = steps.tail ()
        }
    }

//    private String getActiveStepName () {
//        activeStep ? activeStep.name : "no step"
//    }

    private String getActiveScenarioName () {
        activeScenario ? activeScenario.name : "no scenario"
    }
}
