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

import spock.lang.Specification
import cucumber.runtime.RuntimeOptions


class RuntimeOptionsBuilderSpec extends Specification {
    def TAGS = ["@tags1", "@tags2"]
    def FEATURE_PATH = ["test", "cucumber"].join (File.separator)
    def GLUE_PATH = FEATURE_PATH
    def CUSTOM_PATHS = ["path_a", "path_b"]
    def configObject = new ConfigObject ()


    RuntimeOptions createRuntimeOptions (ConfigObject configObject) {
        new RuntimeOptionsBuilder (configObject).build ([])
    }

    RuntimeOptions createRuntimeOptions (ConfigObject configObject, List<String> args) {
        new RuntimeOptionsBuilder (configObject).build (args)
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

    def "feature paths contains only 'String's" () {
        given:
        def g = "G"
        configObject.cucumber.features = ["String", "${g}String"]

        when:
        def featurePaths = createRuntimeOptions (configObject).featurePaths


        then:
        featurePaths*.class == [String.class] * featurePaths.size()
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

    def "glue paths contains only 'String's" () {
        given:
        def g = "G"
        configObject.cucumber.glue = ["String", "${g}String"]

        when:
        def glue = createRuntimeOptions (configObject).glue

        then:
        glue*.class == [String.class] * glue.size()
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

    def "evaluate args if first arg contains ':cucumber'" () {
        given:
        def args = [':cucumber', '@tag']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        ! options.filters.contains (args[0])
        options.filters.contains (args[1])
    }

    def "ignore args if first arg is not ':cucumber'" () {
        given:
        def args = ['functional:', '@tag']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        ! options.filters.contains (args[0])
        ! options.filters.contains (args[1])
    }

    def "tag filter override config filter" () {
        given:
        configObject.cucumber.tags = TAGS
        def args = [':cucumber', '@tag']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.filters.indexOf (TAGS[0]) < 0
        options.filters.indexOf (TAGS[1]) < 0
    }

    def "line filter override config filter" () {
        given:
        configObject.cucumber.tags = TAGS
        def args = [':cucumber', 'some.feature:10']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.filters.indexOf (TAGS[0]) < 0
        options.filters.indexOf (TAGS[1]) < 0
    }

    def "adds auto detected tag filter from cli" () {
        given:
        def args = [':cucumber', '@tag1', '~@tag2', '@tagA,@tagB', '@tag:9']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.filters.contains (args[1])
        options.filters.contains (args[2])
        options.filters.contains (args[3])
        options.filters.contains (args[4])
    }

    def "non tag dir/file args overwrite the feature path" () {
        given:
        configObject.cucumber.defaultFeaturePath = ""
        def args = [':cucumber', 'some dir', 'some.feature']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.featurePaths == ['some dir', 'some.feature']
        options.filters == []
    }

    def "dir/file line filters are added as filter" () {
        given:
        configObject.cucumber.defaultFeaturePath = ""
        def args = [':cucumber', 'some.feature:10:20:30']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.featurePaths == ['some.feature']
        options.filters == [10, 20, 30]
    }

    def "add feature path to any dir/file that does not start with the feature path" () {
        given:
        configObject.cucumber.defaultFeaturePath = FEATURE_PATH
        def args = [':cucumber', 'some.feature', [FEATURE_PATH, 'other.feature'].join (File.separator)]

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.featurePaths[0] == [FEATURE_PATH, 'some.feature'].join (File.separator)
        options.featurePaths[1] == [FEATURE_PATH, 'other.feature'].join (File.separator)
    }

    def "do not add feature path to any dir/file if we have multiple feature paths" () {
        given:
        configObject.cucumber.features = CUSTOM_PATHS
        def args = [':cucumber', 'some.feature']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        options.featurePaths[0] == 'some.feature'
    }

    /*
    def "adds '++name|+n scenario regex' filter from cli" () {
        given:
            configObject.cucumber.cliOptions = [
                '+n',     '@short',
                '++name', '@full',
            ]

        when:
            def options = createRuntimeOptions (configObject)

        then:
            options.filters.size () == 2
            options.filters.find { Pattern p -> p.pattern () == "@short" }
            options.filters.find { Pattern p -> p.pattern () == "@full" }
    }*/
}
