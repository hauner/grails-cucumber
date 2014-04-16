/*
 * Copyright 2014 Martin Hauner
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

import gherkin.formatter.PrettyFormatter
import gherkin.formatter.model.Scenario
import spock.lang.Specification


class PrettyFormatterWrapperDelegatingSpec extends Specification {
    def scenario = Mock (Scenario)
    def formatter = Mock (PrettyFormatter)
    def factory = Mock (PrettyFormatterFactory)
    def uat

    def setup () {
        factory.createFormatter(_) >> formatter
        uat = new PrettyFormatterWrapper (factory)
    }

    def initPrettyFormatter () {
        uat.feature (null)
    }

    def "delegates startOfScenarioLifeCycle() to formatter" () {
        setup:
        initPrettyFormatter ()

        when:
        uat.startOfScenarioLifeCycle (scenario)

        then:
        1 * formatter.startOfScenarioLifeCycle (scenario)
    }

    def "delegates endOfScenarioLifeCycle() to formatter" () {
        setup:
        initPrettyFormatter ()

        when:
        uat.endOfScenarioLifeCycle (scenario)

        then:
        1 * formatter.endOfScenarioLifeCycle (scenario)
    }
}
