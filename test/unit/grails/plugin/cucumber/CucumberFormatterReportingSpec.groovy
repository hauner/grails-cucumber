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

import gherkin.formatter.model.Result
import cucumber.runtime.UndefinedStepException


@SuppressWarnings("GroovyPointlessArithmetic")
class CucumberFormatterReportingSpec extends GherkinSpec {
    FeatureReport report = Mock (FeatureReport)
    def uat = formatter (report)


    def "(re)-init feature report for each feature to create a feature wise log" () {
        given:
        def featureA = featureStub (FEATURE_NAME_A)
        def featureB = featureStub (FEATURE_NAME_B)

        when:
        uat.feature (featureA)
        uat.feature (featureB)

        then:
        1 * report.startFeature (FEATURE_NAME_A)
        1 * report.startFeature (FEATURE_NAME_B)
    }

    def "finishes feature report before initializing a new feature report" () {
        given:
        def featureA = featureStub (FEATURE_NAME_A)
        def featureB = featureStub (FEATURE_NAME_B)

        when:
        uat.feature (featureA)
        uat.feature (featureB)

        then:
        2 * report.startFeature (_)
        1 * report.endFeature ()
    }

    def "report start scenario for each scenario" () {
        given:
        def scenarioA = scenarioStub (SCENARIO_NAME_A)
        def scenarioB = scenarioStub (SCENARIO_NAME_B)

        when:
        uat.scenario (scenarioA)
        uat.scenario (scenarioB)

        then:
        1 * report.startScenario (SCENARIO_NAME_A)
        1 * report.startScenario (SCENARIO_NAME_B)
    }

    // new
    def "report end scenario before a new feature" () {
        given:
        def scenario = scenarioStub ()
        def feature = featureStub ()

        when:
        uat.scenario (scenario)
        uat.feature (feature)

        then:
        (1) * report.endScenario ()

        then:
        (1) * report.startFeature (_)
    }

    def "report scenario end for last scenario" () {
        def featureStub = featureStub (FEATURE_NAME_A)
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)

        when:
        uat.feature (featureStub)
        uat.scenario (scenarioStubA)
        uat.finish ()

        then:
        1 * report.endScenario ()
    }


    def "report end feature for last feature" () {
        def featureStub = featureStub (FEATURE_NAME_A)
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)

        when:
        uat.feature (featureStub)
        uat.scenario (scenarioStubA)
        uat.finish ()

        then:
        1 * report.endFeature ()
    }

    def "reports step failures" () {
        def result = resultStubFail ()

        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub ())
        uat.step (stepStubFail ())
        
        uat.result (result)

        then:
        1 * report.addFailure ((AssertionError)result.error)
    }

    def "reports step errors" () {
        def result = resultStubError ()

        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub ())
        uat.step (stepStubError ())
        uat.result (result)

        then:
        1 * report.addError (result.error)
    }

    def "reports skipped step" () {
        def result = Result.SKIPPED
        
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub ())
        uat.step (stepStub ())
        uat.result (result)

        then:
        1 * report.addSkipped ()
    }
    
    def "reports undefined step" () {
        def result = Result.UNDEFINED
        
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub ())
        uat.step (stepStub ())
        uat.result (result)

        then:
        1 * report.addUndefined ((UndefinedStepException)_)
    }
    
    def "does only report step when it succeeds" () {
        Result result = Mock (Result)
        result.error >> null

        when:
        uat.step (stepStub ())
        uat.result (result)

        then:
        0 * report.addError ((Throwable)_)
        0 * report.addError ((Throwable)_)
    }

    def "reports hook errors when there is no scenario or step" () {
        def result = resultStubError ()

        when:
        uat.feature (featureStub ())
        uat.result (result)

        then:
        1 * report.addError (result.error)
    }
}
