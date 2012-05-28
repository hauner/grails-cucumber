/*
 * Copyright 2012 Martin Hauner
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

import grails.plugin.spock.UnitSpec
import cucumber.runtime.RuntimeOptions


class RuntimeOptionsBuilderSpec extends UnitSpec {
    def TAGS = ["@tags1", "@tags2"]
    def FEATURE_PATH = "test/cucumber"
    def GLUE_PATH = FEATURE_PATH
    def CUSTOM_PATHS = ["path_a", "path_b"]
    def configObject = new ConfigObject ()

    def setup () {
    }

    def "adds tags from configuration to options" () {
        given:
        configObject.cucumber.tags = TAGS

        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.filters == TAGS
    }

    def "does handle no tags configuration" () {
        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.filters.empty
    }

    def "overrides default feature path if features is set" () {
        given:
        configObject.cucumber.features = CUSTOM_PATHS

        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.featurePaths == CUSTOM_PATHS
    }

    def "adds default feature path if features is not set" () {
        given:
        configObject.cucumber.defaultFeaturePath = FEATURE_PATH

        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.featurePaths == [FEATURE_PATH]
    }

    def "overrides default glue path if glue is set" () {
        given:
        configObject.cucumber.glue = CUSTOM_PATHS

        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.glue == CUSTOM_PATHS
    }

    def "adds default glue path if glue is not set" () {
        given:
        configObject.cucumber.defaultGluePath = GLUE_PATH

        when:
        def options = createRuntimeOptions (configObject)

        then:
        options.glue.contains (GLUE_PATH)
    }

    def "disables dry run in options" () {
        when:
        def options = createRuntimeOptions (new ConfigObject ())

        then:
        !options.dryRun
    }

    def "clears default formatter" () {
        when:
        def options = createRuntimeOptions (new ConfigObject ())

        then:
        options.formatters.empty
    }

    RuntimeOptions createRuntimeOptions (ConfigObject configObject) {
        new RuntimeOptionsBuilder (configObject).build ()
    }
}
