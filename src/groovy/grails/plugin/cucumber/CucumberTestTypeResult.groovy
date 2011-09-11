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

import org.codehaus.groovy.grails.test.GrailsTestTypeResult


class CucumberTestTypeResult implements GrailsTestTypeResult {
    int run
    int failures
    int errors

    CucumberTestTypeResult (int runCnt, int failureCnt, int errorCnt) {
        this.run = runCnt
        this.failures = failureCnt
        this.errors = errorCnt
    }

    int getRunCount () {
        run
    }

    int getPassCount () {
        run - getFailCount ()
    }

    int getFailCount () {
        failures + errors
    }
}
