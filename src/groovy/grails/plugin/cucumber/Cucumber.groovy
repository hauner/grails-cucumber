/*
 * Copyright 2011-2015 Martin Hauner
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
import cucumber.api.SummaryPrinter
import cucumber.runtime.Runtime
import cucumber.runtime.model.CucumberFeature


class Cucumber {
    Runtime        runtime
    RuntimeOptions runtimeOptions
    SummaryPrinter summaryPrinter

    List<CucumberFeature> features

    Cucumber (Runtime runtime, RuntimeOptions runtimeOptions, SummaryPrinter summaryPrinter) {
        this.summaryPrinter = summaryPrinter
        this.runtimeOptions = runtimeOptions
        this.runtime = runtime
    }

    void loadFeatures () {
        features = runtimeOptions.cucumberFeatures (runtime)
    }

    int countScenarios () {
        def scenarioCount = 0
        features.each { CucumberFeature feature ->
            scenarioCount += feature.getFeatureElements ().size ()
        }
        scenarioCount
    }

    CucumberTestTypeResult run (CucumberFormatter cucumberFormatter) {
        runtimeOptions.addPlugin (cucumberFormatter)

        // more or less Runtime.run (), we could simply call it...!?
        Formatter formatter = runtimeOptions.getOptionsFormatter (runtime)
        Reporter  reporter  = runtimeOptions.getOptionsReporter (runtime)

        features.each { CucumberFeature feature ->
            feature.run (formatter, reporter, runtime)
        }

        formatter.done ()
        summaryPrinter.print (runtime)
        formatter.close ()

        cucumberFormatter.result
    }
}


