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
import cucumber.runtime.UndefinedStepException


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
        //sysout << "\nFR(startFeature)\n"
        report = factory.createReport (feature)
        suite = factory.createTestSuite (feature)
        report.startTestSuite (suite)
    }

    void endFeature () {
        //sysout << "FR(endFeature)\n"
        report.endTestSuite (suite)
    }

    void startScenario (String scenario) {
        //sysout << "FR(startScenario)\n"
        test = factory.createTest (scenario)
        report.startTest (test)
    }

    void endScenario () {
        //sysout << "FR(endScenario)\n"
        report.endTest (test)
    }

    void addFailure (AssertionError failure) {
        //sysout << "FR(addFailure)\n"
        report.addFailure (test, failure)
    }

    void addError (Throwable error) {
        //sysout << "FR(addError)\n"
        report.addError (test, error)
    }
    
    void addPassed () {
        
    }
    
    void addSkipped () {
        
    }
    
    void addUndefined (UndefinedStepException undefined) {
        report.addFailure (test, undefined)
    }
}
