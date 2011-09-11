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


/*
 * http://jira.grails.org/browse/GRAILS-6453
 * http://grails.1312388.n4.nabble.com/
 *   plugin-classes-not-included-in-classpath-for-plugin-scripts-td2271962.html
 */

includeTargets << grailsScript ("_GrailsCompile")

loadClass = { className ->
    def load = { name ->
        println "### loading ${name}"
        classLoader.loadClass (name)
    }

    try {
        println "### trying to load.."
        load (className)
    } catch (ClassNotFoundException e) {
        println "### catching.."
        compile ()
        load (className)
    }
}

cucumberGrailsTestType = { basedir ->
    loadClass ('grails.plugin.cucumber.CucumberTestType').newInstance (basedir)
}

println "**** cucumberPluginDir: ${cucumberPluginDir}"

cucumberTests = [
    //new CucumberTestType () // fails with class unresolved
]

eventAllTestsStart = {
    println "** Grails All Tests Start **"

    def testType = cucumberGrailsTestType (basedir)

    cucumberTests << testType
    println "** cucumberTests: ${cucumberTests}"

    phasesToRun << testType.NAME
    println "** phasesToRun: ${phasesToRun}"
}

eventAllTestsEnd = {
    println "** Grails All Tests End **"
}

cucumberTestPhasePreparation = {
    println "** Cucumber Test Phase Preparation **"

    //functionalTestPhasePreparation ()
}

cucumberTestPhaseCleanUp = {
    println "** Cucumber Test Phase Clean Up **"

    //functionalTestPhaseCleanUp ()
}

eventTestPhasesStart = { phases ->
    println "** Grails Test PhaseS Start: $phases **"
}

eventTestPhasesEnd = { phases ->
    println "** Grails Test PhaseS End: **"
}

eventTestPhaseStart = { phaseName ->
    println "** Grails Test Phase Start: $phaseName **"
}

eventTestPhaseEnd = { phaseName ->
    println "** Grails Test Phase End: $phaseName **"
}

eventTestSuiteStart = { typeName ->
    println "** Grails Test Suite Start: $typeName **"
}

eventTestSuiteEnd = { typeName ->
    println "** Grails Test Suite End: $typeName **"
}
