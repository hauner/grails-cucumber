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

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest


class FeatureReport {
    FeatureReportHelper factory

    Report report
    JUnitTest suite
    CucumberTest test

    def sysout

    FeatureReport (FeatureReportHelper helper) {
        this.factory = helper
        this.sysout = System.out
    }

    void startFeature (String feature) {
        log.trace ("startFeature ('$feature')\n")

        report = factory.createReport (feature)
        suite = factory.createTestSuite (feature)
        report.startTestSuite (suite)
    }

    void endFeature () {
        log.trace ("endFeature ()\n")

        report.endTestSuite (suite)
    }

    void startScenario (String scenario) {
        log.trace ("  startScenario ('$scenario')\n")

        test = factory.createTest (scenario)
        report.startTest (test)
    }

    void endScenario () {
        log.trace ("  endScenario ()\n")

        report.endTest (test)
    }

    void addFailure (AssertionError failure) {
        log.trace ("    addFailure (...)")

        report.addFailure (test, failure)
    }

    void addError (Throwable error) {
        log.trace ("    addError (...)\n")

        report.addError (test, error)
    }
    
    void addPassed () {
        log.trace ("    addPassed ()\n")
    }
    
    void addSkipped (Throwable error) {
        log.trace ("    addSkipped (...)\n")

        report.addFailure (test, error)
    }

    void addUndefined (Throwable undefined) {
        log.trace ("    addUndefined (...)\n")

        report.addFailure (test, undefined)
    }
}
