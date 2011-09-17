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
import org.codehaus.groovy.grails.test.report.junit.JUnitReports


class FeatureReportSpec extends UnitSpec {
    def helper = Mock (FeatureReportHelper)
    def uat = new FeatureReport (helper)

    @SuppressWarnings(["GroovyPointlessArithmetic", "GroovyAssignabilityCheck"])
    def "create new report for each feature" () {
        helper.createReport (!null) >> Mock (JUnitReports)

        when:
        uat.startFeature (FEATURE_NAME_A)
        uat.startFeature (FEATURE_NAME_B)

        then:
        1 * helper.createReport (FEATURE_NAME_A)
        1 * helper.createReport (FEATURE_NAME_B)
    }

    def "x" () {

    }

}
