/*
 * Copyright 2011 Martin Hauner
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

import gherkin.formatter.Reporter
import gherkin.formatter.Formatter
import cucumber.resources.Consumer
import cucumber.resources.Resource
import cucumber.resources.Resources
import cucumber.runtime.Runtime
import cucumber.runtime.model.CucumberFeature
import cucumber.runtime.FeatureBuilder
import cucumber.runtime.snippets.SummaryPrinter


class Cucumber {
    String featureDir

    def features = new ArrayList<CucumberFeature> ()
    def filters = new ArrayList ()
    def paths = new ArrayList<String>()
    def runtime

    Cucumber (String featureDir) {
        this.featureDir = featureDir
        paths.add (featureDir)

        runtime = new Runtime (paths, false)
    }

    void loadFeatures () {
        // duplicates code from cucumber Runtime.load
        FeatureBuilder builder = new FeatureBuilder (features)

        // load gherkin files
        Resources.scan (featureDir, ".feature",
          new Consumer() {
            @Override
            public void consume (Resource resource) {
              builder.parse (resource, filters)
            }
          }
        )
    }

    int countScenarios () {
        def scenarioCount = 0
        features.each {
            scenarioCount += it.getFeatureElements().size ()
        }
        scenarioCount
    }

    void run (Formatter formatter, Reporter reporter) {
        runtime.run (paths, filters, formatter, reporter)
    }

    void printSummary (PrintStream stream) {
        new SummaryPrinter (stream).print (runtime);
    }
}


