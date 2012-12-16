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

import org.codehaus.groovy.grails.test.report.junit.JUnitReports
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import junit.framework.AssertionFailedError


class ReportSpec extends GherkinSpec {
    static long START_TIME = 10
    static long STOP_TIME = 10
    static long DURATION = STOP_TIME - START_TIME
    static String OUT_TEXT = "out"
    static String ERR_TEXT = "err"

    PrintStream out = Mock (PrintStream)
    PrintStream err = Mock (PrintStream)
    JUnitReports report = Mock (JUnitReports)
    SystemOutAndErrSwapper swapper = Mock (SystemOutAndErrSwapper)
    Timer timer = Mock (Timer)

    def uat = new Report (report, swapper, timer)


    def "starts timer when starting a test suite" () {
        when:
        uat.startTestSuite (null)

        then:
        (1) * timer.start ()
    }

    def "stops timer when ending the test suite" () {
        swapper.swapOut() >> [out, err]
        def suite = Mock (JUnitTest)

        when:
        uat.endTestSuite (suite)

        then:
        (1) * timer.stop ()
    }

    def "reports start of test suite" () {
        def suite = Mock (JUnitTest)

        when:
        uat.startTestSuite (suite)

        then:
        (1) * report.startTestSuite (suite)
    }

    def "reports end of test suite" () {
        swapper.swapOut() >> [out, err]
        def suite = Mock (JUnitTest)

        when:
        uat.endTestSuite (suite)

        then:
        (1) * report.endTestSuite (suite)
    }

    def "sets run time when ending the test suite" () {
        swapper.swapOut() >> [out, err]
        def suite = Mock (JUnitTest)
        timer.runtime () >> DURATION

        when:
        uat.startTestSuite (suite)
        uat.endTestSuite (suite)

        then:
        (1) * suite.setRunTime (DURATION)
    }

    def "switches output streams when starting a test suite" () {
        when:
        uat.startTestSuite (null)

        then:
        (1) * swapper.swapIn ()
    }

    def "restores output streams when ending the test suite" () {
        out.toString () >> OUT_TEXT
        err.toString () >> ERR_TEXT
        def suite = Mock (JUnitTest)

        when:
        uat.endTestSuite (suite)

        then:
        (1) * swapper.swapOut () >> [out, err]
        (1) * report.setSystemOutput (OUT_TEXT)
        (1) * report.setSystemError (ERR_TEXT)
    }

    def "reports start & end of test" () {
        def test = Mock (CucumberTest)

        when:
        uat.startTest (test)
        uat.endTest (test)

        then:
        (1) * report.startTest (test)
        (1) * report.endTest (test)
    }

    def "counts tests" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)

        when:
            uat.startTest (new CucumberTest ())
            uat.startTest (new CucumberTest ())
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts (2, (Long)_, (Long)_)
    }

    def "counts failures" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)

        when:
            uat.addFailure (new CucumberTest (), null)
            uat.addFailure (new CucumberTest (), null)
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts ((Long)_, 2, (Long)_)
    }

    def "counts errors" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)

        when:
            uat.addError (new CucumberTest (), null)
            uat.addError (new CucumberTest (), null)
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts ((Long)_, (Long)_, 2)
    }

    def "counts each test only once" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)
            def test = new CucumberTest ()

        when:
            uat.startTest (test)
            uat.startTest (test)
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts (1, (Long)_, (Long)_)
    }

    def "counts each failure only once" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)
            def test = new CucumberTest ()

        when:
            uat.addFailure (test, null)
            uat.addFailure (test, null)
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts ((Long)_, 1, (Long)_)
    }

    def "counts each error only once" () {
        given:
            swapper.swapOut() >> [out, err]
            def suite = Mock (JUnitTest)
            def test = new CucumberTest ()

        when:
            uat.addError (test, null)
            uat.addError (test, null)
            uat.endTestSuite (suite)

        then:
            1 * suite.setCounts ((Long)_, (Long)_, 1)
    }

    def "report assertion error as failure" () {
        def test = Mock (CucumberTest)
        def failure = Mock (AssertionError)

        when:
        uat.addFailure (test, failure)

        then:
        1 * report.addFailure (test, _ as AssertionFailedError)
    }

    def "report throwable as failure" () {
        def test = Mock (CucumberTest)
        def failure = Mock (Throwable)

        when:
        uat.addFailure (test, failure)

        then:
        1 * report.addFailure (test, _)
    }

    def "reports error" () {
        def test = Mock (CucumberTest)
        def failure = Mock (Throwable)

        when:
        uat.addError (test, failure)

        then:
        (1) * report.addError (test, failure)
    }

    def "starting test prints header to out & err stream" () {
        given:
            swapper.swapIn () >> [out, err]
            def suite = Mock (JUnitTest)
            def test = Mock (CucumberTest)
            test.toString () >> "TESTNAME"
            def header = uat.header (test)

        when:
            uat.startTestSuite (suite)
            uat.startTest (test)

        then:
            (1) * out.write (header.bytes)
            (1) * err.write (header.bytes)
    }
}
