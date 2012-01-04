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

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import gherkin.formatter.model.Result


@SuppressWarnings("GroovyPointlessArithmetic")

class CucumberFormatterSignalingSpec extends GherkinSpec {
    def publisher = Mock (GrailsTestEventPublisher)
    def formatter = formatter (publisher)
    

    def "signals test case start for each new feature" () {
        given:
        def featureStubA = featureStubA ()
        def featureStubB = featureStubB ()

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        1 * publisher.testCaseStart (featureStubA.name)
        1 * publisher.testCaseStart (featureStubB.name)
    }

    def "signals test case end for previous feature before signaling a new feature" () {
        given:
        def featureStubA = featureStubA ()
        def featureStubB = featureStubB ()

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        1 * publisher.testCaseEnd (featureStubA.name)
        1 * publisher.testCaseStart (featureStubB.name)
    }

    def "signals test case end for last feature" () {
        given:
        def featureStubA = featureStubA ()
        def featureStubB = featureStubB ()
        def scenarioStub = scenarioStub ()

        when:
        formatter.feature (featureStubA)
        formatter.scenario (scenarioStub)
        formatter.feature (featureStubB)
        formatter.scenario (scenarioStub)
        formatter.finish ()

        then:
        1 * publisher.testCaseEnd (featureStubB.name)
    }

    def "signals test start for each new scenario" () {
        given:
        def scenarioStubA = scenarioStubA ()
        def scenarioStubB = scenarioStubB ()

        when:
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)

        then:
        1 * publisher.testStart (scenarioStubA.name)
        1 * publisher.testStart (scenarioStubB.name)
    }

    def "signals test end for previous scenario before signaling a new scenario" () {
        given:
        def scenarioStubA = scenarioStubA ()
        def scenarioStubB = scenarioStubB ()

        when:
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)

        then:
        1 * publisher.testEnd (scenarioStubA.name)
        1 * publisher.testStart (scenarioStubB.name)
    }

    def "signals test end for last scenario" () {
        def featureStub = featureStub ()
        def scenarioStubA = scenarioStubA ()
        def scenarioStubB = scenarioStubB ()

        when:
        formatter.feature (featureStub)
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)
        formatter.finish ()

        then:
        1 * publisher.testEnd (scenarioStubB.name)
    }

    def "signals test failure after failing step" () {
        given:
        def stepStub = stepStub ()
        def resultStub = resultStubFail ()
        formatter.feature (featureStub ())
        formatter.scenario (scenarioStub ())        
        formatter.step (stepStub)

        when:
        formatter.result (resultStub)

        then:
        1 * publisher.testFailure (stepStub.name, resultStub.error)
    }

    def "signals test error after erroneous step" () {
        given:
        def stepStub = stepStub ()
        def resultStub = resultStubError ()
        formatter.feature (featureStub ())
        formatter.scenario (scenarioStub ())        
        formatter.step (stepStub)

        when:
        formatter.result (resultStub)

        then:
        1 * publisher.testFailure (stepStub.name, resultStub.error, true)
    }

    def "signals test failure after undefined step" () {
        given:
        def stepStub = stepStub ()
        def resultStub = Result.UNDEFINED
        formatter.feature (featureStub ())
        formatter.scenario (scenarioStub ())        
        formatter.step (stepStub)

        when:
        formatter.result (resultStub)

        then:
        1 * publisher.testFailure (stepStub.name, "undefined")
    }

    def "NOT signals test failure after skipped step" () {
        given:
        def stepStub = stepStub ()
        def resultStub = Result.SKIPPED
        formatter.feature (featureStub ())
        formatter.scenario (scenarioStub ())        
        formatter.step (stepStub)

        when:
        formatter.result (resultStub)

        then:
        0 * publisher.testFailure (stepStub.name, "skipped")
    }
    
    def "signals test failure for current step" () {
        given:
        def stepStubA = stepStub (STEP_NAME_A)
        def stepStubB = stepStub (STEP_NAME_B)
        formatter.feature (featureStub ())
        formatter.scenario (scenarioStub ())
        formatter.step (stepStubA)
        formatter.step (stepStubB)

        when:
        formatter.result (resultStubPass ())        
        formatter.result (resultStubFail ())

        then:
        1 * publisher.testFailure (stepStubB.name, (Throwable)_)
    }
}
