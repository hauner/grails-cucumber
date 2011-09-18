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

import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest


class FeatureReportHelper {
    SystemOutAndErrSwapper swapper
    JUnitReportsFactory factory

    FeatureReportHelper (JUnitReportsFactory factory, SystemOutAndErrSwapper swapper) {
        this.factory = factory
        this.swapper = swapper
    }

    Report createReport (String feature) {
        new Report (factory.createReports (feature), swapper, new Timer ())
    }

    JUnitTest createTestSuite (String feature) {
        new JUnitTest (feature)
    }

    CucumberTest createTest (String scenario) {
        new CucumberTest (scenario)
    }

}
