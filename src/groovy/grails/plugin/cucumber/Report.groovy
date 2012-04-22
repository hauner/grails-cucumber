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

import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.codehaus.groovy.grails.test.report.junit.JUnitReports
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest


class Report {
    SystemOutAndErrSwapper swapper
    Timer timer

    JUnitReports report
    long startTime
    def streams

    long failures
    long errors
    long runs

    Report (JUnitReports report, SystemOutAndErrSwapper swapper, Timer timer) {
        this.report = report
        this.swapper = swapper
        this.timer = timer
    }

    void startTestSuite (JUnitTest suite) {
        timer.start ()
        streams = swapper.swapIn ()
        report.startTestSuite (suite)
    }


    void endTestSuite (JUnitTest suite) {
        timer.stop ()
        suite.runTime = timer.runtime ()
        suite.setCounts (runs, failures, errors)

        def (out, err) = swapper.swapOut()*.toString ()
        report.systemOutput = out
        report.systemError = err
        report.endTestSuite (suite)
    }


    void startTest (CucumberTest test) {
        streams*.println (header (test))
        report.startTest (test)
        runs++
    }

    void endTest (CucumberTest test) {
        report.endTest (test)
    }

    void addFailure (CucumberTest test, Throwable failure) {
        report.addFailure (test, new FakeAssertionFailedError (failure))
        failures++
    }

    void addError (CucumberTest test, Throwable error) {
        report.addError (test, error)
        errors++
    }

    String header (CucumberTest test) {
        return "\n--Output from ${test}--"
    }
}
