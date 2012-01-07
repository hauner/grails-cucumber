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

import gherkin.formatter.Reporter
import gherkin.formatter.Formatter
import cucumber.runtime.Runtime
import cucumber.runtime.model.CucumberFeature
import cucumber.runtime.snippets.SummaryPrinter
import cucumber.io.FileResourceLoader
import cucumber.runtime.groovy.GroovyBackend

class Cucumber {
    String featureDir

    def features
    def filters = new ArrayList ()
    def paths = new ArrayList<String>()
    def resourceLoader
    def backends
    def runtime

    Cucumber (GroovyShell shell, String featureDir) {
        this.featureDir = featureDir
        paths.add (featureDir)

        resourceLoader = new FileResourceLoader ()
        backends = [new GroovyBackend (shell, resourceLoader)]
        runtime = new Runtime (paths, resourceLoader, backends, false)
    }

    void loadFeatures () {
        features = CucumberFeature.load (resourceLoader, paths, filters)
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


