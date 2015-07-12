/*
 * Copyright 2011-2013, 2015 Martin Hauner
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

import org.codehaus.groovy.grails.compiler.GrailsProjectCompiler


GrailsProjectCompiler projectCompiler = new GrailsProjectCompiler(pluginSettings)
projectCompiler.configureClasspath()


// compile step code given by CucumberConfig:cucumber.sources = [] after anything else was compiled.
eventTestCompileEnd = { testType ->
    if (testType.toString() != 'cucumber') {
        return
    }

    List sourceDirs = testType.getGlueSources (grailsSettings.config)
    sourceDirs = sourceDirs - ["test/functional"]  // grails compiles this automatically
    if (sourceDirs.empty) {
        return
    }

    projectCompiler.srcDirectories.addAll (sourceDirs)
    projectCompiler.compile ([testDirPath, currentTestPhaseName].join (File.separator))
}


eventTestPhasesStart = { phases ->
    if (! phases.contains ('functional')) {
        return
    }

    def testType = loadTestType()
    [functional: functionalTests].each { name, types ->
        if (!types.any {it.class == testType}) {
            types << testType.newInstance (name)
        }
    }
}


def loadTestType () {
    // we have to soft load the test type class:
    // http://jira.grails.org/browse/GRAILS-6453
    // http://grails.1312388.n4.nabble.com/plugin-classes-not-included-in-classpath-for-plugin-scripts-td2271962.html
    loadClass ('grails.plugin.cucumber.CucumberTestType')
}

loadClass = { className ->
    def load = { name ->
        classLoader.loadClass (name)
    }

    try {
        load (className)
    } catch (ClassNotFoundException ignored) {
        projectCompiler.compileAll()
        load (className)
    }
}
