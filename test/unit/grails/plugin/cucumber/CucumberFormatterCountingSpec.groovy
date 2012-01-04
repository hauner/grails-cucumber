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


class CucumberFormatterCountingSpec extends GherkinSpec {
    def uat = formatter ()

    def "counts total number of scenarios" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.scenario (scenarioStub (SCENARIO_NAME_B))

        then:
        uat.getResult ().getRunCount () == 2
        uat.getResult ().getPassCount () == 2
    }

    def "counts number of scenarios with failing step" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.step (stepStub (STEP_NAME_PASS))
        uat.result (resultStubPass ())
        uat.scenario (scenarioStub (SCENARIO_NAME_B))
        uat.step (stepStub (STEP_NAME_FAIL))
        uat.result (resultStubFail ())

        then:
        uat.getResult ().getRunCount () == 2
        uat.getResult ().getPassCount () == 1
        uat.getResult ().getFailCount () == 1
    }

    def "counts number of scenarios with erroneous step" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.step (stepStub (STEP_NAME_PASS))
        uat.result (resultStubPass ())
        uat.scenario (scenarioStub (SCENARIO_NAME_B))
        uat.step (stepStub (STEP_NAME_FAIL))
        uat.result (resultStubError ())

        then:
        uat.getResult ().getRunCount () == 2
        uat.getResult ().getPassCount () == 1
        uat.getResult ().getFailCount () == 1
    }

    def "count failing scenario only once with any number of non passing steps" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.step (stepStub (STEP_NAME_PASS))
        uat.step (stepStub (STEP_NAME_FAIL))
        uat.result (resultStubFail ())
        uat.result (resultStubFail ())

        then:
        uat.getResult ().getRunCount () == 1
        uat.getResult ().getPassCount () == 0
        uat.getResult ().getFailCount () == 1
    }
    
    def "do not count scenario with skipped step as failure" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.step (stepStub (STEP_NAME_FAIL))
        uat.result (Result.SKIPPED)

        then:
        uat.getResult ().getRunCount () == 1
        uat.getResult ().getPassCount () == 1
        uat.getResult ().getFailCount () == 0
    }

    def "count scenario with undefined step as failure" () {
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub (SCENARIO_NAME_A))
        uat.step (stepStub (STEP_NAME_FAIL))
        uat.result (Result.UNDEFINED)

        then:
        uat.getResult ().getRunCount () == 1
        uat.getResult ().getPassCount () == 0
        uat.getResult ().getFailCount () == 1
    }

    def "does report scenario either as failed or erroneous" () {
        def resultFail = resultStubFail ()
        def resultError = resultStubError ()
        
        when:
        uat.feature (featureStub ())
        uat.scenario (scenarioStub ())
        uat.step (stepStubFail ())
        uat.step (stepStubError ())
        
        uat.result (resultFail)
        uat.result (resultError)

        then:
        uat.getResult ().getRunCount () == 1
        uat.getResult ().getPassCount () == 0
        uat.getResult ().getFailCount () == 1
    }
}

