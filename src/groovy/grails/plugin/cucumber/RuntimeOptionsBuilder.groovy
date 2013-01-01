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
import cucumber.runtime.PathWithLines


class RuntimeOptionsBuilder {
    def configObject

    RuntimeOptionsBuilder (ConfigObject configObject) {
        this.configObject = configObject
    }

    RuntimeOptions build (List<String> args) {
        def options = new RuntimeOptions (new Properties ())

        setTags (options)
        setFormatter (options)
        setGluePaths (options)
        setFeaturePaths (options)

        addCliFilter (options, args)
        //addCliFilter (options) //-Dcucumber.options='--tags @ignore --name .*bla.* file/feature:8:9'

        options
    }

    def addCliFilter (RuntimeOptions options, List<String> args) {
        if (args.size() < 2 || ! args.first().contains(":cucumber")) {
            return
        }

        def filters = []
        def featurePaths = []
        args.each { arg ->
            switch (arg) {
                case ~/\w*:cucumber/:
                    break;
                case ~/~?@.+(:\d)?/:
                    filters << arg
                    break;
                case ~/.+\.feature(:\d+)*/:
                    PathWithLines pathWithLines = new PathWithLines(arg)
                    featurePaths << "$configObject.cucumber.defaultFeaturePath/$pathWithLines.path".toString()
                    filters += pathWithLines.lines
                    break
            }
        }

        if (filters) {
            options.filters.clear()
            options.filters.addAll(filters)
        }
        if (featurePaths) {
            options.featurePaths.clear()
            options.featurePaths.addAll(featurePaths)
        }
    }

    // obsolete
    def addCliFilter (RuntimeOptions options) {
        def args = configObject.cucumber.cliOptions
        if (!args)
            return

        options.filters.clear ()

        while (!args.empty) {
            String arg = args.remove (0)

            switch (arg) {
                case ['++tags', '+t']:
                    options.filters << args.remove (0)
                    break
                case ['++name', '+n']:
                    options.filters << ~args.remove (0)
                    break
                default:
                    PathWithLines pathWithLines = new PathWithLines (arg.replace ('|', ':'))
                    options.featurePaths << pathWithLines.path
                    options.filters << pathWithLines.lines
            }
        }
    }

    private void setGluePaths (RuntimeOptions options) {
        if (configObject.cucumber.glue) {
            options.glue.clear ()
            configObject.cucumber.glue.each { path ->
                options.glue << path.toString ()  // *NO* GString
            }
        } else {
            options.glue << configObject.cucumber.defaultGluePath
        }
    }

    private void setFeaturePaths (RuntimeOptions options) {
        if (configObject.cucumber.features) {
            options.featurePaths.clear ()
            configObject.cucumber.features.each { path ->
                options.featurePaths << path.toString ()  // *NO* GString
            }
        } else {
            options.featurePaths << configObject.cucumber.defaultFeaturePath
        }
    }

    private void setFormatter (RuntimeOptions options) {
        // clear the 'default' cucumber formatter
        options.formatters.clear ()
    }

    private void setTags (RuntimeOptions options) {
        configObject.cucumber.tags.each {
            options.filters.add (it)
        }
    }

}
