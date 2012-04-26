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

import grails.test.AbstractCliTestCase


class TestAppTests extends AbstractCliTestCase {
    static final int TEST_COUNT = 4
    static final int TEST_PASSED = 1
    static final int TEST_FAILED = 3

    /* cli test are very slow, so we test the output of a single run */
    void testCucumberTestOutput () {
        runTestApp ()
        verifyHeader ()

        verifyRunning ()
        verifyTestFeaturePassed ()
        verifyTestFeatureAssertionFailure ()
        verifyTestFeatureExceptionError ()
        verifyTestFeatureMissingSnippets ()
        verifyTestCounts ()
    }


    private void runTestApp () {
        execute (["test-app", "functional:cucumber"])
        waitForProcess ()
    }

    private void verifyRunning () {
        assertTrue (
            "running output not match",
            output.contains ("Running " +TEST_COUNT+ " cucumber tests...")
        )
    }

    private void verifyTestFeaturePassed () {
        // nothing reported
    }

    private void verifyTestFeatureAssertionFailure () {
        assertTrue (
            "assertion failure does not match exeption",
            output.contains ("Caused by: java.lang.AssertionError")
        )

        assertTrue (
            "assertion failure does not match file & line",
            output.contains ('RunTestApp_steps$_run_closure4.doCall(RunTestApp_steps.groovy:')
        )
    }

    private void verifyTestFeatureExceptionError () {
        assertTrue (
            "exception error does not match exeption",
            output.contains ("java.lang.ArithmeticException: Division by zero")
        )

        assertTrue (
            "exception error does not match step",
            output.contains ("at ✽.Then it should error(RunTestAppWithIssues.feature:16)")
        )
    }


    private void verifyTestFeatureMissingSnippets () {
        assertTrue (
            "missing step header does not match",
            output.contains ("You can implement missing steps with the snippets below:")
        )

        assertTrue (
            "missing unimplemented step snippet",
            output.contains ("When(~'^an unimplemented step is found\$')")
        )
    }

    private void verifyTestCounts () {
        assertTrue (
            "test counts do not match",
            output.contains ("Completed " +TEST_COUNT+ " cucumber tests, " +TEST_FAILED+ " failed")
        )
    }
}
