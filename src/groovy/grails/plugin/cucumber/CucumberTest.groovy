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

import junit.framework.Test
import junit.framework.TestResult
import org.junit.runner.Describable
import org.junit.runner.Description


class CucumberTest implements Test, Describable {
    String description

    CucumberTest () {
        //System.out << "CT(CucumberTest)\n"
        this.description = "CT(DESCRIPTION)"
    }

    CucumberTest (description) {
        //System.out << "CT(CucumberTest($description))\n"
        this.description = description
    }

    int countTestCases () {
        //System.out << "CT(countTestCases)\n"
        return 1
    }

    void run (TestResult result) {
        throw new RuntimeException ("this test can not be run!");
    }

    @Override
    String toString () {
        //System.out << "CT(toString:$description)\n"
        return description
    }

    Description getDescription () {
        //System.out << "CT(getDescription)\n"
        //return Description.createSuiteDescription ("CucumberTest")
        return Description.createTestDescription(getClass (), description/*"CucumberTest"*/)
    }

    /* provides "test name" for report */
    String getName () {
        description
    }
}

