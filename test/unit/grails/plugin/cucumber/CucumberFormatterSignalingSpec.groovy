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
import cucumber.runtime.UndefinedStepException


class CucumberFormatterSignalingSpec extends GherkinSpec {
    GrailsTestEventPublisher publisher = Mock (GrailsTestEventPublisher)
    def formatter = formatter (publisher)
    

    def "signals test case start for each new feature" () {
        given:
        def featureStubA = featureStubA ()
        def featureStubB = featureStubB ()

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        (1) * publisher.testCaseStart (featureStubA.name)
        (1) * publisher.testCaseStart (featureStubB.name)
    }

    def "signals test end and test case end of previous feature before signaling a new feature" () {
        given:
        def featureStubA = featureStubA ()
        def scenarioStub = scenarioStub ()
        def featureStubB = featureStubB ()

        when:
        formatter.feature (featureStubA)
        formatter.scenario (scenarioStub)
        formatter.feature (featureStubB)

        then:
        (1) * publisher.testEnd (scenarioStub.name)

        then:
        (1) * publisher.testCaseEnd (featureStubA.name)
    }

    def "signals test case end for previous feature before signaling a new feature" () {
        given:
        def featureStubA = featureStubA ()
        def featureStubB = featureStubB ()

        when:
        formatter.feature (featureStubA)
        formatter.feature (featureStubB)

        then:
        (1) * publisher.testCaseEnd (featureStubA.name)
        (1) * publisher.testCaseStart (featureStubB.name)
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
        (1) * publisher.testCaseEnd (featureStubB.name)
    }

    def "signals test start for each new scenario" () {
        given:
        def scenarioStubA = scenarioStubA ()
        def scenarioStubB = scenarioStubB ()

        when:
        formatter.scenario (scenarioStubA)
        formatter.scenario (scenarioStubB)

        then:
        (1) * publisher.testStart (scenarioStubA.name)
        (1) * publisher.testStart (scenarioStubB.name)
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
        (1) * publisher.testEnd (scenarioStubB.name)
    }

    def "signals scenario failure after failing step" () {
        given:
            def resultStub = resultStubFail ()
            def scenarioStub = scenarioStub ()
            formatter.feature (featureStub ())
            formatter.scenario (scenarioStub)
            formatter.step (stepStub ())

        when:
            formatter.result (resultStub)

        then:
            (1) * publisher.testFailure (scenarioStub.name, resultStub.error)
    }

    def "signals scenario error after erroneous step" () {
        given:
            def resultStub = resultStubError ()
            def scenarioStub = scenarioStub ()
            formatter.feature (featureStub ())
            formatter.scenario (scenarioStub)
            formatter.step (stepStub ())

        when:
            formatter.result (resultStub)

        then:
            (1) * publisher.testFailure (scenarioStub.name, resultStub.error, true)
    }

    def "signals scenario failure after undefined step" () {
        given:
            def resultStub = Result.UNDEFINED
            def scenarioStub = scenarioStub ()
            formatter.feature (featureStub ())
            formatter.scenario (scenarioStub)
            formatter.step (stepStub ())

        when:
            formatter.result (resultStub)

        then:
            //1 * publisher.testFailure (stepStub.name, "undefined") crashes IDEA
            (1) * publisher.testFailure (scenarioStub.name, (Throwable)_)
    }

    def "do no signal scenario failure after skipped step without before hook failure" () {
        given:
            def resultStub = Result.SKIPPED
            def scenarioStub = scenarioStub ()
            formatter.feature (featureStub ())
            formatter.scenario (scenarioStub)
            formatter.step (stepStub ())

        when:
            formatter.result (resultStub)

        then:
            0 * publisher.testFailure (scenarioStub.name, (String)_)
    }

    def "signals scenario failure after skipped step *with* before hook failure" () {
        given:
            def resultStub = Result.SKIPPED
            def scenarioStub = scenarioStub ()
            formatter.feature (featureStub ())
            def beforeStub = resultStubFail ()
            formatter.before (null, beforeStub)
            formatter.scenario (scenarioStub)
            formatter.step (stepStub ())

        when:
            formatter.result (resultStub)

        then:
            1 * publisher.testFailure (scenarioStub.name, beforeStub.error)
    }
}
