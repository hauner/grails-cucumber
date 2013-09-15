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

package grails.plugin.cucumber

import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import grails.util.Environment
import cucumber.runtime.Runtime
import cucumber.runtime.RuntimeOptions
import cucumber.runtime.groovy.GroovyBackend
import cucumber.runtime.snippets.SummaryPrinter
import cucumber.runtime.io.MultiLoader


class CucumberTestType extends GrailsTestTypeSupport {
    static final ENVIRONMENT = Environment.TEST.name
    static final CONFIG_NAME = "CucumberConfig.groovy"
    static final CONFIG_PATH = ["grails-app", "conf", CONFIG_NAME].join (File.separator)
    static final NAME = "cucumber"

    Cucumber cucumber


    CucumberTestType (String testPhase) {
        super (NAME, testPhase)
    }

    @Override
    List<String> getTestExtensions () {
        ["feature"]
    }

    @Override
    int doPrepare () {
        prepareCucumber ()
        loadFeatures ()
        countScenarios ()
    }

    @Override
    GrailsTestTypeResult doRun (GrailsTestEventPublisher eventPublisher) {
        runFeatures (eventPublisher)
    }

    @Override
    String toString () {
        NAME
    }

    private void prepareCucumber () {
        def classLoader = getTestClassLoader ()

        def multiLoader = new MultiLoader (classLoader)
        def groovyShell = new GroovyShell (classLoader, createBinding ())
        def groovyBackend = new GroovyBackend (groovyShell, multiLoader)

        def summaryPrinter = new SummaryPrinter (System.out)
        def runtimeOptions = buildOptions (buildBinding.argsMap.params)
        def runtime = new Runtime (multiLoader, classLoader, [groovyBackend], runtimeOptions)

        cucumber = new Cucumber (multiLoader, runtime, runtimeOptions, summaryPrinter)
    }

    private RuntimeOptions buildOptions (def args) {
        def configSlurper = new ConfigSlurper (ENVIRONMENT)
        configSlurper.setBinding ([
            basedir: buildBinding.basedir,
            testDirPath: buildBinding.testDirPath
        ])
        def configReader = new ConfigReader (new File (CONFIG_PATH), configSlurper)
        def configObject = configReader.parse ()

        configObject.cucumber.defaultFeaturePath = featurePath ()
        configObject.cucumber.defaultGluePath = featurePath ()

        new RuntimeOptionsBuilder (configObject).build (args)
    }

    // called from _Events.groovy to get the source dirs we should compile
    static List getGlueSources () {
        def configSlurper = new ConfigSlurper (ENVIRONMENT)
        def configReader = new ConfigReader (new File (CONFIG_PATH), configSlurper)
        def configObject = configReader.parse ()

        (configObject.cucumber.sources) ?: []
    }

    private Binding createBinding () {
        Map variables = buildBinding.variables.clone () as Map
        variables.remove ("metaClass")
        variables.remove ("getMetaClass")
        variables.remove ("setMetaClass")

        // grails 2.3 does not provide this anymore!?
        if(!variables.containsKey('appCtx')) {
            variables.put('appCtx', getApplicationContext())
        }

        new Binding (variables)
    }

    private void loadFeatures () {
        cucumber.loadFeatures ()
    }

    private int countScenarios () {
        cucumber.countScenarios ()
    }

    private GrailsTestTypeResult runFeatures (def publisher) {
        cucumber.run (createFormatter (publisher))
    }

    private def createFormatter (def publisher) {
        def swapper = createSystemOutAndErrSwapper ()
        def factory = createJUnitReportsFactory ()

        def report = new FeatureReport (new FeatureReportHelper (factory, swapper))
        def pretty = new PrettyFormatterWrapper (new PrettyFormatterFactory ())

        new CucumberFormatter (publisher, report, pretty, pretty)
        //new DebugFormatter (System.out, pretty)
    }

    private JUnitReportsFactory createJUnitReportsFactory () {
        JUnitReportsFactory.createFromBuildBinding (buildBinding)
    }

    private String featurePath () {
        ["test", relativeSourcePath].join (File.separator)
    }
}
