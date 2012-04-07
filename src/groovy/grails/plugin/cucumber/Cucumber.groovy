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

import cucumber.runtime.Runtime
import cucumber.runtime.model.CucumberFeature
import cucumber.runtime.snippets.SummaryPrinter
import cucumber.runtime.RuntimeOptions
import cucumber.io.ResourceLoader


class Cucumber {
    def summaryPrinter
    def resourceLoader
    def runtimeOptions
    def runtime

    def features

    Cucumber (ResourceLoader resourceLoader, Runtime runtime, RuntimeOptions runtimeOptions,
        SummaryPrinter summaryPrinter) {
        this.summaryPrinter = summaryPrinter
        this.resourceLoader = resourceLoader
        this.runtimeOptions = runtimeOptions
        this.runtime = runtime
    }

    void loadFeatures () {
        features = runtimeOptions.cucumberFeatures (resourceLoader)
    }

    int countScenarios () {
        def scenarioCount = 0
        features.each { CucumberFeature feature ->
            scenarioCount += feature.getFeatureElements ().size ()
        }
        scenarioCount
    }

    CucumberTestTypeResult run (CucumberFormatter formatter) {
        //runtimeOptions.formatters << formatter

        features.each { CucumberFeature feature ->
            feature.run (formatter, formatter, runtime)
            //private: _runtime.run (feature)
        }

        // todo merge finish into done
        formatter.finish ()
        formatter.done ()
        summaryPrinter.print (runtime)
        formatter.close ()
        formatter.result
    }
}


