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

import static GherkinSpec.*
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import junit.framework.AssertionFailedError
import spock.lang.Specification


class FeatureReportSpec extends Specification {
    def report
    def factory
    def uat

    def setup () {
        report = Mock (Report)
        factory = Mock (FeatureReportHelper)
        factory.createReport (!null) >> report
        uat = new FeatureReport (factory)
    }

    // fails, does the same createReport() as the next two tests, which work!?
    /*
    def "creates new report for each feature" () {
        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        1 * factory.createReport (FEATURE_NAME_A)
        1 * factory.createReport (FEATURE_NAME_B)
    }
    */

    def "creates new test suite for each feature" () {
        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        (1) * factory.createTestSuite (FEATURE_NAME_A)
        (1) * factory.createTestSuite (FEATURE_NAME_B)
    }

    def "starts a test suite for each feature" () {
        def suiteA = Mock (JUnitTest)
        def suiteB = Mock (JUnitTest)
        factory.createTestSuite (!null) >>> [suiteA, suiteB]

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        (1) * report.startTestSuite (suiteA)
        (1) * report.startTestSuite (suiteB)
    }

    def "ends the test suite for each feature" () {
        def suiteA = Mock (JUnitTest)
        def suiteB = Mock (JUnitTest)
        factory.createTestSuite (!null) >>> [suiteA, suiteB]

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.endFeature ()
        uat.startFeature (FEATURE_NAME_B)
        uat.endFeature ()

        then:
        (1) * report.endTestSuite (suiteA)
        (1) * report.endTestSuite (suiteB)
    }

    def "starts a test for each scenario" () {
        def testA = Mock (CucumberTest)
        def testB = Mock (CucumberTest)
        factory.createTest (!null) >>> [testA, testB]

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME_A)
        uat.startScenario (SCENARIO_NAME_B)

        then:
        (1) * report.startTest (testA)
        (1) * report.startTest (testB)
    }

    def "ends the test for each scenario" () {
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
        (1) * report.endTest (testA)
        (1) * report.endTest (testB)
    }

    def "reports failure as failure" () {
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

    def "reports error as error" () {
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

    def "reports undefined as failure" () {
        def undefined = Mock (Throwable)
        def test = Mock (CucumberTest)
        factory.createTest (!null) >> test

        when:
        uat.startFeature (FEATURE_NAME)
        uat.startScenario (SCENARIO_NAME)
        uat.addUndefined (undefined)

        then:
        1 * report.addFailure (test, undefined)
    }
}

