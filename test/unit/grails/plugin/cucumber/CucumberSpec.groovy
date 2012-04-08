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
import cucumber.io.ResourceLoader
import cucumber.runtime.Runtime
import cucumber.runtime.RuntimeOptions
import cucumber.runtime.model.CucumberFeature
import cucumber.runtime.model.CucumberTagStatement
import cucumber.runtime.snippets.SummaryPrinter


class CucumberSpec extends UnitSpec {
    private static final int SCENARIO_COUNT_A = 2
    private static final int SCENARIO_COUNT_B = 3
    def summaryPrinter = Mock (SummaryPrinter)
    def resourceLoader = Mock (ResourceLoader)
    def options = Mock (RuntimeOptions)
    def runtime = Mock (Runtime)

    class OptionsStub extends RuntimeOptions {
        def features

        OptionsStub (features) {
            super ([])
            formatters.clear ()
            this.features = features
        }

        @Override
        List<CucumberFeature> cucumberFeatures (ResourceLoader resourceLoader) {
            features
        }
    }

    def setup () {

    }

    def "should load features" () {
        given:
        def cucumber = new Cucumber (resourceLoader, runtime, options, null)

        when:
        cucumber.loadFeatures ()

        then:
        (1) * options.cucumberFeatures (resourceLoader)
    }

    def "should count scenarios" () {
        given:
        options.cucumberFeatures (_) >> [
            featureStub (scenarioStubs (SCENARIO_COUNT_A)),
            featureStub (scenarioStubs (SCENARIO_COUNT_B))
        ]
        def cucumber = new Cucumber (resourceLoader, runtime, options, null)
        cucumber.loadFeatures ()

        when:
        int countScenarios = cucumber.countScenarios ()

        then:
        countScenarios == SCENARIO_COUNT_A + SCENARIO_COUNT_B
    }

    def "should run features with formatter" () {
        given:
        def formatter = Mock (CucumberFormatter)
        def featureA = featureStub (null)
        def featureB = featureStub (null)
        options.cucumberFeatures (_) >> [featureA, featureB]

        def cucumber = new Cucumber (resourceLoader, runtime, options, summaryPrinter)
        cucumber.loadFeatures ()

        when:
        cucumber.run (formatter)

        then:
        (1) * featureA.run (formatter, formatter, runtime)
        (1) * featureB.run (formatter, formatter, runtime)

        then:
        (1) * formatter.finish ()

        then:
        (1) * formatter.done ()
    }

    def "should report summary after finish ()/done () and before close ()" () {
        given:
        def formatter = Mock (CucumberFormatter)
        options.cucumberFeatures (_) >> []

        def cucumber = new Cucumber (resourceLoader, runtime, options, summaryPrinter)
        cucumber.loadFeatures ()

        when:
        cucumber.run (formatter)

        then:
        (1) * formatter.finish ()

        then:
        (1) * formatter.done ()

        then:
        (1) * summaryPrinter.print (runtime)

        then:
        (1) * formatter.close ()
    }

    def "should return formatter result after running features" () {
        given:
        def formatter = Mock (CucumberFormatter)
        def expectedResult = Mock (CucumberTestTypeResult)
        formatter.getResult() >> expectedResult
        options.cucumberFeatures (_) >> []

        def cucumber = new Cucumber (resourceLoader, runtime, options, summaryPrinter)
        cucumber.loadFeatures ()

        when:
        def result = cucumber.run (formatter)

        then:
        result == expectedResult
    }

    // build stubs
    def featureStub (def scenarios) {
        def feature = Mock (CucumberFeature)
        feature.getFeatureElements () >> scenarios
        feature
    }

    def scenarioStubs (def count) {
        def scenarios = []
        count.times {
            scenarios << Mock (CucumberTagStatement)
        }
        scenarios
    }
}
