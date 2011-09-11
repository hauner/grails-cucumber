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

import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.report.junit.JUnitReports
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import junit.framework.AssertionFailedError


class FeatureReportHelper {
    SystemOutAndErrSwapper swapper
    JUnitReportsFactory factory

    FeatureReportHelper (SystemOutAndErrSwapper swapper, JUnitReportsFactory factory) {
        this.swapper = swapper
        this.factory = factory
    }

    List<OutputStream> replaceOutAndErr () {
        swapper.swapIn ()
    }

    List<OutputStream> restoreOutAndErr () {
        swapper.swapOut ()
    }

    JUnitReports createReport (String feature) {
        factory.createReports (feature)
    }

    JUnitTest createTestSuite (String feature) {
        new JUnitTest (feature)
    }

    long now () {
        System.currentTimeMillis ()
    }
}


class FeatureReport {
    FeatureReportHelper helper
    JUnitReports report

    long start
    JUnitTest suite
    CucumberTest test

    int runCount
    int failureCount
    int errorCount

    def sysout

    FeatureReport (FeatureReportHelper helper) {
        this.helper = helper
        this.sysout = System.out
    }

    void startFeature (String feature) {
        sysout << "\nFR(startFeature)\n"
        //helper.replaceOutAndErr ()

        report = helper.createReport (feature)
        suite = helper.createTestSuite (feature)

        report.startTestSuite (suite)
        start = helper.now ()
    }

    void endFeature () {
        sysout << "FR(endFeature)\n"

        long runtime = helper.now () - start

        suite.runTime = runtime
        suite.setCounts (runCount, failureCount, errorCount)

//        def (out, err) = helper.restoreOutAndErr ()*.toString ()

        //        report.systemOutput = out
        //        report.systemError = err
        report.endTestSuite (suite)
    }

    void startScenario (String scenario) {
        sysout << "FR(startScenario)\n"
        [System.out, System.err]*.println ("\n--Output from ${scenario}--")

        test = new CucumberTest (scenario)
        report.startTest (test)

        runCount++
    }

    void endScenario () {
        sysout << "FR(endScenario)\n"
        report.endTest (test)
    }

    void addFailure (AssertionFailedError failure) {
        report.addFailure (test, failure)
        failureCount++
    }

    void addError (Throwable error) {
        report.addError (test, error)
        errorCount++
    }
}
