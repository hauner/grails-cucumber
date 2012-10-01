/*
 * Copyright 2011-2012 Martin Hauner
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

    void finish () {
        log.trace ("finish ()\n")

        endScenario ()
        endFeature ()
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

    void step (Step step) {
        steps.add (step)
        formatter.step (step)
    }

    void eof () {
        formatter.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, Integer line) {
        formatter.syntaxError (state, event, legalEvents, uri, line)
    }

    void done () {
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
            if (result.error instanceof AssertionError) {
                report.addFailure ((AssertionError)result.error)
                publisher.testFailure (getActiveScenarioName (), result.error)
                
                fail (getActiveScenarioName ())
            }
            else {
                report.addError (result.error)
                publisher.testFailure (getActiveScenarioName (), result.error, true)
                
                error (getActiveScenarioName ())
            }            
        }
        else if (result == Result.SKIPPED) {
            if (beforeResult) {
                report.addSkipped (beforeResult.error)
                publisher.testFailure (activeScenarioName, beforeResult.error)
                fail (getActiveScenarioName ())

                beforeResult = null
                beforeMatch = null
            }
//            else {
//                //report.addSkipped (null)
//                //publisher.testFailure (activeScenarioName, "skipped")
//                //fail (getActiveScenarioName ())
//            }
        }
        else if (result == Result.UNDEFINED) {
            def error = new UndefinedStepException (activeStep)
            report.addUndefined (error)
            publisher.testFailure (activeScenarioName, error)
            
            fail (getActiveScenarioName ())
        }
        else if (result.status == Result.PASSED) {
            report.addPassed ()
        }
        
        reporter.result (result)
    }

    void after (Match match, Result result) {
        reporter.after (match, result)
    }

    private void fail (String scenario) {
        if (erroneous.count (scenario) == 0) {
            failed.add (scenario)
        }
    }
    
    private void error (String scenario) {
        if (failed.count (scenario) == 0) {
            erroneous.add (scenario)
        }
    }

    private void advanceActiveStep () {
        if (steps.size() > 0) {
            activeStep = steps.head ()
            steps = steps.tail ()
        }
    }

    private String getActiveStepName () {
        if (! activeStep) {
            return "no step"
        }
        activeStep.name
    }

    private String getActiveScenarioName () {
        if (! activeScenario) {
            return "no scenario"
        }
        activeScenario.name
    }
}
