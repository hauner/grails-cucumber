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


class RuntimeOptionsBuilder {
    def configObject

    RuntimeOptionsBuilder (ConfigObject configObject) {
        this.configObject = configObject
    }

    RuntimeOptions build () {
        def options = new RuntimeOptions ()

        setTags (options)
        setFormatter (options)
        setGluePaths (options)
        setFeaturePaths (options)

        options
    }

    private void setGluePaths (RuntimeOptions options) {
        def fixedGlue = configObject.cucumber.fixedGluePath
        options.glue << fixedGlue

        def glue = configObject.cucumber.glue
        if (glue) {
            options.glue = configObject.cucumber.glue
        } else {
            options.glue << configObject.cucumber.defaultGluePath
        }
    }

    private void setFeaturePaths (RuntimeOptions options) {
        def features = configObject.cucumber.features

        if (features) {
            options.featurePaths = features
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
