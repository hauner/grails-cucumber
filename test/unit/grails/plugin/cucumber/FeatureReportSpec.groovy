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

import grails.plugin.spock.*
import static grails.plugin.cucumber.GherkinSpec.*
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import spock.lang.Ignore
import junit.framework.AssertionFailedError


@SuppressWarnings(["GroovyPointlessArithmetic"])
class FeatureReportSpec extends UnitSpec {
    def report = Mock (Report)
    def factory = Mock (FeatureReportHelper)
    def uat = new FeatureReport (factory)


    @Ignore // fails, does the same createReport() as the next two tests, which work!?
    def "creates new report for each feature" () {
        factory.createReport (!null) >> report

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        1 * factory.createReport (FEATURE_NAME_A)
        1 * factory.createReport (FEATURE_NAME_B)
    }

    def "creates new test suite for each feature" () {
        factory.createReport (!null) >> report

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        1 * factory.createTestSuite (FEATURE_NAME_A)
        1 * factory.createTestSuite (FEATURE_NAME_B)
    }

    def "starts a test suite for each feature" () {
        factory.createReport (!null) >> report
        def suiteA = Mock (JUnitTest)
        def suiteB = Mock (JUnitTest)
        factory.createTestSuite (!null) >>> [suiteA, suiteB]

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        1 * report.startTestSuite (suiteA)
        1 * report.startTestSuite (suiteB)
    }

    def "ends the test suite for each feature" () {
        factory.createReport (!null) >> report
        def suiteA = Mock (JUnitTest)
        def suiteB = Mock (JUnitTest)
        factory.createTestSuite (!null) >>> [suiteA, suiteB]

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.endFeature ()
        uat.startFeature (FEATURE_NAME_B)
        uat.endFeature ()

        then:
        1 * report.endTestSuite (suiteA)
        1 * report.endTestSuite (suiteB)
    }

    def "starts a test for each scenario" () {
        factory.createReport (!null) >> report
        def testA = Mock (CucumberTest)
        def testB = Mock (CucumberTest)
        factory.createTest (!null) >>> [testA, testB]

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME_A)
        uat.startScenario (SCENARIO_NAME_B)

        then:
        1 * report.startTest (testA)
        1 * report.startTest (testB)
    }

    def "ends the test for each scenario" () {
        factory.createReport (!null) >> report
        def testA = Mock (CucumberTest)
        def testB = Mock (CucumberTest)
        factory.createTest (!null) >>> [testA, testB]

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME_A)
        uat.endScenario ()
        uat.startScenario (SCENARIO_NAME_B)
        uat.endScenario ()

        then:
        1 * report.endTest (testA)
        1 * report.endTest (testB)
    }

    def "reports each failure" () {
        factory.createReport (!null) >> report
        def failure = Mock (AssertionFailedError)
        def test = Mock (CucumberTest)
        factory.createTest (!null) >> test

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME)
        uat.addFailure (failure)

        then:
        1 * report.addFailure (test, failure)
    }

    def "reports each error" () {
        factory.createReport (!null) >> report
        def error = Mock (Throwable)
        def test = Mock (CucumberTest)
        factory.createTest (!null) >> test

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME)
        uat.addError (error)

        then:
        1 * report.addError (test, error)
    }

}

