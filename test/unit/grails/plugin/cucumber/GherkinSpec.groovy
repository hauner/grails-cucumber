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

import grails.plugin.spock.UnitSpec
import spock.lang.Ignore

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.Step
import gherkin.formatter.model.Result
import gherkin.formatter.model.Background
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Match
import gherkin.formatter.Formatter
import gherkin.formatter.Reporter


@Ignore
class GherkinSpec extends UnitSpec {
    static def FEATURE_NAME = "Test Feature"
    static def FEATURE_NAME_A = "Test Feature A"
    static def FEATURE_NAME_B = "Test Feature B"
    static def BACKGROUND_NAME = "Test Background"
    static def SCENARIO_NAME = "Test Scenario"
    static def SCENARIO_NAME_A = "Test Scenario A"
    static def SCENARIO_NAME_B = "Test Scenario B"
    static def SCENARIO_OUTLINE_NAME = "Test Scenario Outline"
    static def EXAMPLES_NAME = "Test Examples"
    static def STEP_NAME = "Test Step"
    static def STEP_NAME_A = "Test Step A"
    static def STEP_NAME_B = "Test Step B"
    static def STEP_NAME_PASS = "Test Step PASS"
    static def STEP_NAME_FAIL = "Test Step FAIL"
    static def STEP_NAME_ERROR = "Test Step ERROR"
    static def STEP_NAME_UNDEF_A = "Test Step UNDEFINED A"
    static def STEP_NAME_UNDEF_B = "Test Step UNDEFINED B"

    static def dummyUri = "Test Dummy URI"

    def featureStub () {
        featureStub (FEATURE_NAME)
    }

    def featureStubA () {
        featureStub (FEATURE_NAME_A)
    }
    
    def featureStubB () {
        featureStub (FEATURE_NAME_B)
    }

    def featureStub (String name) {
        def stub = Mock (Feature)
        stub.getName () >> name
        stub
    }

    def backgroundStub () {
        def stub = Mock (Background)
        stub.getName () >> BACKGROUND_NAME
        stub
    }

    def scenarioStub () {
        scenarioStub (SCENARIO_NAME)
    }

    def scenarioStub (String name) {
        def stub = Mock (Scenario)
        stub.getName () >> name
        stub
    }

    def scenarioStubA () {
        scenarioStub (SCENARIO_NAME_A)
    }

    def scenarioStubB () {
        scenarioStub (SCENARIO_NAME_B)
    }
    
    def scenarioOutlineStub () {
        def stub = Mock (ScenarioOutline)
        stub.getName () >> SCENARIO_OUTLINE_NAME
        stub
    }

    def examplesStub () {
        def stub = Mock (Examples)
        stub.getName () >> EXAMPLES_NAME
        stub
    }

    def stepStub () {
        stepStub (STEP_NAME)
    }

    def stepStub (String name) {
        def stub = Mock (Step)
        stub.getName () >> name
        stub
    }
    
    def stepStubFail () {
        stepStub (STEP_NAME_FAIL)
    }

    def stepStubError () {
        stepStub (STEP_NAME_ERROR)
    }

    def resultStubPass () {
        def stub = Mock (Result)
        stub.status >> Result.PASSED
        stub
    }
    
    def resultStubFail () {
        def stub = Mock (Result)
        stub.status >> Result.FAILED
        stub.error >> new AssertionError ()
        stub
    }

    def resultStubError () {
        def stub = Mock (Result)
        stub.status >> Result.FAILED
        stub.error >> new Throwable ()
        stub
    }
    
    def matchStub () {
        Mock (Match)
    }

    def formatter () {
        new CucumberFormatter (
            Mock (GrailsTestEventPublisher),
            Mock (FeatureReport),
            Mock (Formatter),
            Mock (Reporter)
        )
    }

    def formatter (FeatureReport report) {
        new CucumberFormatter (
            Mock (GrailsTestEventPublisher),
            report,
            Mock (Formatter),
            Mock (Reporter)
        )
    }

    def formatter (GrailsTestEventPublisher publisher) {
        new CucumberFormatter (
            publisher,
            Mock (FeatureReport),
            Mock (Formatter),
            Mock (Reporter)
        )
    }

    def formatter (Formatter formatter, Reporter reporter) {
        new CucumberFormatter (
            Mock (GrailsTestEventPublisher),
            Mock (FeatureReport),
            formatter,
            reporter
        )
    }
}
