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

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import junit.framework.AssertionFailedError


@SuppressWarnings("GroovyPointlessArithmetic")

class CucumberFormatterSignalingSpec extends GherkinSpec {

    def "signals test case start for each new feature" () {
        given:
        def featureStubA = featureStub (FEATURE_NAME_A)
        def featureStubB = featureStub (FEATURE_NAME_B)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        1 * publisher.testCaseStart (FEATURE_NAME_A)
        1 * publisher.testCaseStart (FEATURE_NAME_B)
    }

    def "signals test case end for previous feature before signaling a new feature" () {
        given:
        def featureStubA = featureStub (FEATURE_NAME_A)
        def featureStubB = featureStub (FEATURE_NAME_B)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        1 * publisher.testCaseEnd (FEATURE_NAME_A)
        1 * publisher.testCaseStart (FEATURE_NAME_B)
    }

    def "signals test case end for last feature" () {
        def featureStubA = featureStub (FEATURE_NAME_A)
        def featureStubB = featureStub (FEATURE_NAME_B)
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.feature (featureStubA)
        formatter.scenario (scenarioStubA)
        formatter.feature (featureStubB)
        formatter.scenario (scenarioStubA)
        formatter.finish ()

        then:
        1 * publisher.testCaseEnd (FEATURE_NAME_B)
    }

    def "signals test start for each new scenario" () {
        given:
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)
        def scenarioStubB = scenarioStub (SCENARIO_NAME_B)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)

        then:
        1 * publisher.testStart (SCENARIO_NAME_A)
        1 * publisher.testStart (SCENARIO_NAME_B)
    }

    def "signals test end for previous scenario before signaling a new scenario" () {
        given:
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)
        def scenarioStubB = scenarioStub (SCENARIO_NAME_B)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)

        then:
        1 * publisher.testEnd (SCENARIO_NAME_A)
        1 * publisher.testStart (SCENARIO_NAME_B)
    }

    def "signals test end for last scenario" () {
        def featureStub = featureStub (FEATURE_NAME_A)
        def scenarioStubA = scenarioStub (SCENARIO_NAME_A)
        def scenarioStubB = scenarioStub (SCENARIO_NAME_B)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.feature (featureStub)
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)
        formatter.finish ()

        then:
        1 * publisher.testEnd (SCENARIO_NAME_B)
    }

    def "signals test failure after failing step" () {
        def stepStub = stepStub (STEP_NAME_A)
        def error = new AssertionFailedError ()
        def resultStub = resultStub (error)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.step (stepStub)
        formatter.result (resultStub)

        then:
        1 * publisher.testFailure (STEP_NAME_A, error)
    }

    def "signals test error after erroneous step" () {
        def stepStub = stepStub (STEP_NAME_A)
        def error = new NullPointerException ()
        def resultStub = resultStub (error)
        def publisher = Mock (GrailsTestEventPublisher)
        def formatter = formatter (publisher)

        when:
        formatter.step (stepStub)
        formatter.result (resultStub)

        then:
        1 * publisher.testFailure (STEP_NAME_A, error, true)
    }

}
