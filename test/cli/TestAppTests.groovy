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

import grails.test.AbstractCliTestCase


class TestAppTests extends AbstractCliTestCase {
    static final int TEST_COUNT = 4
    static final int TEST_PASSED = 2
    static final int TEST_FAILED = 2

    void testAppRunsCucumberTestPhase () {
        execute (["test-app", "cucumber:"])
        waitForProcess ()

        verifyHeader ()
        verifyPhase ()
    }

    void testAppReportsNumberOfScenariosThatWillRun () {
        execute (["test-app", "cucumber:"])
        waitForProcess ()

        verifyTestCounts ()
    }

    void testAppReportsSuccessfulTestFeatures () {
        execute (["test-app", "cucumber:"])
        waitForProcess ()

        verifyTestFeaturePassed ()
    }

    void testAppReportsIssuesOfTestFeatures () {
        execute (["test-app", "cucumber:"])
        waitForProcess ()

        verifyTestFeatureIssues ()
    }

    void testAppReportsStepSnippets () {
        execute (["test-app", "cucumber:"])
        waitForProcess ()

        verifyTestFeatureSnippets ()
    }

    private void verifyPhase () {
        assertTrue output.contains ("Starting cucumber test phase ...")
    }

    private void verifyTestCounts () {
        assertTrue output.contains ("Running " +TEST_COUNT+ " cucumber tests...")
        assertTrue output.contains ("Tests passed: " +TEST_PASSED)
        assertTrue output.contains ("Tests failed: " +TEST_FAILED)
    }

    private void verifyTestFeaturePassed () {
        assertTrue output.contains ("Running test test-app with succeeding features...PASSED")
    }

    private void verifyTestFeatureIssues () {
        assertTrue output.contains ("report the failing step...FAILED")
        assertTrue output.contains ("report the erroneous step...FAILED")
    }

    private void verifyTestFeatureSnippets () {
        assertTrue output.contains ("You can implement missing steps with the snippets below")
        // fails if checked as single string with \n line feeds
        assertTrue output.contains ("And(~\"^an unimplemented step is found\$\") { ->")
        assertTrue output.contains ("    // Express the Regexp above with the code you wish you had")
        assertTrue output.contains ("}")
    }
}
