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

import cucumber.runtime.RuntimeOptions
import spock.lang.Specification


class RuntimeOptionsBuilderSpec extends Specification {
    def TAGS = ["@tags1", "@tags2"]
    def FEATURE_PATH = "test/cucumber"
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

    def "evaluate cli if first arg contains ':cucumber'" () {
        given:
        def args = [':cucumber', '@tag']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        ! options.filters.contains (args[0])
        options.filters.contains (args[1])
    }

    def "ignore cli if first arg is not ':cucumber'" () {
        given:
        def args = ['functional:', '@tag']

        when:
        def options = createRuntimeOptions (configObject, args)

        then:
        ! options.filters.contains (args[0])
        ! options.filters.contains (args[1])
    }

    def "cli filter override config filter" () {
        given:
        configObject.cucumber.tags = TAGS
        def args = [':cucumber', 'anything']

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

    /*
    def "adds '[[FILE|DIR|URL][|LINE[|LINE]*]]+' filter from cli" () {
        given:
            configObject.cucumber.cliOptions = [
                'my/feature/a|1',
                'my/feature/b|1|2'
            ]

        when:
            def options = createRuntimeOptions (configObject)

        then:
            options.featurePaths.contains('my/feature/a')
            options.featurePaths.contains('my/feature/b')
            options.filters.size () == 2
            options.filters.contains([1L])
            options.filters.contains([1L, 2L])
    }*/
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
    /*
    def "gives warning when multiple filter types are given on cli" () {
        given:
            configObject.cucumber.cliOptions = [
                '+n',     '@short',
                '++name', '@full',
                'my/feature/a:1'
            ]

    }
    */

}
