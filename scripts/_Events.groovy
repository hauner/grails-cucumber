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

includeTargets << grailsScript ("_GrailsCompile")


loadClass = { className ->
    def load = { name ->
        classLoader.loadClass (name)
    }

    try {
        load (className)
    } catch (ClassNotFoundException e) {
        compile ()
        load (className)
    }
}

//  See the following links to understand why we have to manually load the test type class.
//  http://jira.grails.org/browse/GRAILS-6453
//  http://grails.1312388.n4.nabble.com/plugin-classes-not-included-in-classpath-for-plugin-scripts-td2271962.html


eventAllTestsStart = {
    //println "** Grails All Tests Start **"

    if (!binding.variables.containsKey ("functionalTests")) {
        return
    }

    def testType = loadClass ('grails.plugin.cucumber.CucumberTestType')

    [functional: functionalTests].each { name, types ->
        if (!types.any {it.class == testType}) {
            types << testType.newInstance (name,
                grailsSettings.baseDir.path, grailsSettings.testClassesDir.path)
        }
    }
}

/*
eventAllTestsEnd = {
    //println "** Grails All Tests End **"
}
*/
