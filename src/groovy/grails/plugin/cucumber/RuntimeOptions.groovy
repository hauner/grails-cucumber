/*
 * Copyright 2013-2014 Martin Hauner
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

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import cucumber.runtime.Runtime
import cucumber.runtime.model.CucumberFeature


class RuntimeOptions extends cucumber.runtime.RuntimeOptions {

    RuntimeOptions () {
        super ([])
    }

    @SuppressWarnings ("GroovyAccessibility")
    List<CucumberFeature> cucumberFeatures (Runtime runtime) {
        cucumberFeatures (runtime.resourceLoader)
    }

    @SuppressWarnings ("GroovyAccessibility")
    Formatter getOptionsFormatter (Runtime runtime) {
        formatter (runtime.classLoader)
    }

    @SuppressWarnings ("GroovyAccessibility")
    Reporter getOptionsReporter (Runtime runtime) {
        reporter (runtime.classLoader)
    }

}
