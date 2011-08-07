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
 http://grails.1312388.n4.nabble.com/plugin-classes-not-included-in-classpath-for-plugin-scripts-td2271962.html
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

cucumberGrailsTestType = {
    loadClass ('CucumberGrailsTestType').newInstance ()
}

cuke4DukeInstaller = { p ->
    loadClass ('Cuke4DukeInstaller').newInstance (p)
}


println "**** cucumberPluginDir: ${cucumberPluginDir}"

cucumberTests = [
    // does not work, class unresolved
    //new CucumberGrailsTestType ()
]




install = {
    installer = cuke4DukeInstaller ('XXXXX')
    installer.run ()
}

eventAllTestsStart = {
    println "** Grails All Tests Start **"

    def testType = cucumberGrailsTestType ()

    cucumberTests << testType
    println "** cucumberTests: ${cucumberTests}"

    phasesToRun << testType.NAME
    //phasesToRun << CucumberGrailsTestType.NAME
    println "** phasesToRun: ${phasesToRun}"

    install ()
}

eventAllTestsEnd = {
    println "** Grails All Tests End **"
}

cucumberTestPhasePreparation = {
    println "** Cucumber Test Phase Preparation **"

    //functionalTestPhasePreparation()
}

cucumberTestPhaseCleanUp = {
    println "** Cucumber Test Phase Clean Up **"

    //functionalTestPhaseCleanUp()
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



//println "BINDING: ${binding.variables}"