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
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.Step
import gherkin.formatter.model.Background
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Result
import gherkin.formatter.model.Match

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import junit.framework.AssertionFailedError


class CucumberFormatter implements Formatter, Reporter {
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

    CucumberFormatter (GrailsTestEventPublisher publisher, FeatureReport report,
        Formatter formatter, Reporter reporter) {
        this.publisher = publisher
        this.formatter = formatter
        this.reporter = reporter
        this.report = report

        sysout = System.out
    }

    void finish () {
        //sysout << "CF(finish)\n"

        report.endScenario ()
        publisher.testEnd (activeScenario.getName ())

        report.endFeature ()
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
            //sysout << "CF(feature end)\n"

            report.endFeature ()
            publisher.testCaseEnd (activeFeature.getName ())
        }

        //sysout << "CF(feature start)\n"
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
            //sysout << "CF(scenario end)\n"

            report.endScenario ()
            publisher.testEnd (activeScenario.getName ())
        }

        //sysout << "CF(scenario start)\n"
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
        //sysout << "CF(step)\n"

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

    void result (Result result) {
        //sysout << "CF(result)\n"

        if (result.error != null) {
            
            if (result.error instanceof AssertionFailedError) {
                report.addFailure ((AssertionFailedError)result.error)
                publisher.testFailure (activeStep.getName (), result.error)

                failureCount++
            }
            else {
                report.addError (result.error)
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
