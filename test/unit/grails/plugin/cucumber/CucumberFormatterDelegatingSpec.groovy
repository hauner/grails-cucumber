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

import gherkin.formatter.Formatter
import gherkin.formatter.Reporter


@SuppressWarnings("GroovyPointlessArithmetic")
class CucumberFormatterDelegatingSpec extends GherkinSpec {
    def formatter = Mock (Formatter)
    def reporter = Mock (Reporter)
    def uat = formatter (formatter, reporter)

    
    def "delegates uri() to formatter" () {
        when:
        uat.uri (dummyUri)

        then:
        1 * formatter.uri (dummyUri)
    }

    def "delegates feature() to formatter" () {
        given:
        def feature = featureStub ()

        when:
        uat.feature (feature)

        then:
        1 * formatter.feature (feature)
    }

    def "delegates background() to formatter" () {
        given:
        def background = backgroundStub ()

        when:
        uat.background (background)

        then:
        1 * formatter.background (background)
    }

    def "delegates scenario() to formatter" () {
        given:
        def scenario = scenarioStub ()

        when:
        uat.scenario (scenario)

        then:
        1 * formatter.scenario (scenario)
    }

    def "delegates scenarioOutline() to formatter" () {
        given:
        def outline = scenarioOutlineStub ()

        when:
        uat.scenarioOutline (outline)

        then:
        1 * formatter.scenarioOutline (outline)
    }

    def "delegates examples() to formatter" () {
        given:
        def examples = examplesStub ()

        when:
        uat.examples (examples)

        then:
        1 * formatter.examples (examples)
    }

    def "delegates step() to formatter" () {
        given:
        def step = stepStub ()

        when:
        uat.step (step)

        then:
        1 * formatter.step (step)
    }

    def "delegates eof() to formatter" () {
        when:
        uat.eof ()

        then:
        1 * formatter.eof ()
    }

    def "delegates syntaxError() to formatter" () {
        when:
        uat.syntaxError ("", "", null, "", 0)

        then:
        1 * formatter.syntaxError (_, _, _, _, 0)
    }

    def "delegates before() to reporter" () {
        given:
            def match = matchStub ()
            def result = resultStubPass ()

        when:
            uat.before (match, result)

        then:
            1 * reporter.before (match, result)
    }

    def "delegates result() to reporter" () {
        given:
        def step = stepStub ()
        def result = resultStubPass ()

        when:
        uat.step (step)
        uat.result (result)

        then:
        1 * reporter.result (result)
    }

    def "delegates after() to reporter" () {
        given:
            def match = matchStub ()
            def result = resultStubPass ()

        when:
            uat.after (match, result)

        then:
            1 * reporter.after (match, result)
    }

    def "delegates match() to reporter" () {
        given:
        def match = matchStub ()

        when:
        uat.match (match)

        then:
        1 * reporter.match (match)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "delegates embedding() to reporter" () {
        when:
        uat.embedding ("", null)

        then:
        1 * reporter.embedding (_, _)
    }

}
