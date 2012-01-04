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

    /* cli test are very slow, so we test all conditions in a single test */
    void testCucumberTestOutput () {
        runTestApp ()


        verifyHeader ()
        verifyPhase ()
        
        verifyTestFeaturePassed ()
        verifyTestFeatureIssues ()
        verifyTestFeatureSnippets ()
        
        verifyTestCounts ()
    }

    
    private void runTestApp () {
        execute (["test-app", "functional:cucumber"])
        waitForProcess ()
    }
    
    private void verifyPhase () {
        assertTrue (
            "phase does not match",
            output.contains ("Starting functional test phase ...")
        )
    }

    private void verifyTestFeaturePassed () {
        assertTrue (
            "passed feature does not match",
            output.contains ("Running test test-app with a passing cucumber feature...PASSED")
        )
    }
    
    private void verifyTestFeatureIssues () {
        assertTrue (
            "failing step does not match",
            output.contains ("it should fail...FAILED")
        )
        assertTrue (
            "erroneous step does not match",
            output.contains ("it should error...FAILED")
        )
    }

    private void verifyTestFeatureSnippets () {
        assertTrue (
            "missing step header does not match",
            output.contains ("You can implement missing steps with the snippets below:")
        )

        assertTrue (
            "missing unimplemented step snippet",
            output.contains ('When(~"^an unimplemented step is found$")')
        )
    }
    
    private void verifyTestCounts () {
        assertTrue (
            "number of tests does not match",
            output.contains ("Running " +TEST_COUNT+ " cucumber tests...")
        )
        assertTrue (
            "number of passed tests does not match",
            output.contains ("Tests passed: " +TEST_PASSED)
        )
        assertTrue (
            "number of failed tests does not match",
            output.contains ("Tests failed: " +TEST_FAILED)
        )
    }
}
