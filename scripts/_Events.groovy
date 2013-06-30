/*
 * Copyright 2011-2013 Martin Hauner
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


// compile step code given by CucumberConfig:cucumber.sources = [] after anything else was compiled.
eventCompileEnd = {
    def testType = loadTestType()

    def sourceDirs = testType.getGlueSources ()
    sourceDirs = sourceDirs - ["test/functional"]  // grails compiles this automatically
    if (sourceDirs.empty) {
        return
    }

    try {
        boolean verbose = grailsSettings.verboseCompile // true

        // compile to  testDirPath, defaults to ../target/test-classes
        File dst = new File ([testDirPath, "functional"].join (File.separator))

        def classpathId = "grails.test.classpath"
        ant.mkdir (dir: dst)
        ant.groovyc (
            destdir: dst, classpathref: classpathId, verbose: verbose,
            listfiles: verbose) {
                javac(classpathref: classpathId, debug: "yes")
                src {
                    sourceDirs.collect { dir ->
                        pathelement (location: dir)
                    }
                }
            }
    }
    catch (e) {
        grailsConsole.error ("""Compilation error compiling cucumber glue code:
            ${e.cause ? e.cause.message : e.message}""", e.cause ? e.cause : e)
        exit 1
    }
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
        compile ()
        load (className)
    }
}
