/*
 * Copyright 2011-2015 Martin Hauner
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
import cucumber.runtime.DefaultSummaryPrinter
import cucumber.runtime.groovy.GroovyBackend
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
        def runtimeOptions = initOptions (new RuntimeOptions ())

        def runtime = new Runtime (multiLoader, classLoader, [groovyBackend], runtimeOptions)

        cucumber = new Cucumber (runtime, runtimeOptions, new DefaultSummaryPrinter ())
    }

    private RuntimeOptions initOptions (RuntimeOptions options) {
        def configObject = getConfig(buildBinding.grailsSettings.config, [
                basedir: buildBinding.basedir,
                testDirPath: buildBinding.testDirPath
            ])

        configObject.cucumber.defaultFeaturePath = featurePath ()
        configObject.cucumber.defaultGluePath = featurePath ()

        new RuntimeOptionsBuilder (configObject).init (options, buildBinding.argsMap)
        options
    }

    // called from _Events.groovy
    public List getGlueSources (def grailsConfig) {
        (getConfig(grailsConfig).cucumber.sources) ?: []
    }

    private static def getConfig(def grailsConfig, Map binding = [:]) {
        def configSlurper = new ConfigSlurper (ENVIRONMENT)
        configSlurper.setBinding (binding)

        def configReader = new ConfigReader (new File (CONFIG_PATH), configSlurper)

        if (configReader.exists ()) {
            return configReader.parse ()
        }
        else {
            return grailsConfig
        }
    }

    private Binding createBinding () {
        Map variables = buildBinding.variables.clone () as Map
        variables.remove ("metaClass")
        variables.remove ("getMetaClass")
        variables.remove ("setMetaClass")
        setAppCtx (variables)
        setFunctionalTestBaseUrl (variables)
        new Binding (variables)
    }

    private void setAppCtx (Map variables) {
        // appCtx is no longer available in the (test-app) binding since grails 2.3

        // for plugin backward compatibility we add it if possible, i.e. not forked!
        if (!forked && !variables.containsKey('appCtx')) {
            variables.put('appCtx', getApplicationContext())
        }
    }

    private void setFunctionalTestBaseUrl (Map variables) {
        // functionalBaseUrl may not be in the (test-app) binding since grails 2.3

        if (!variables.containsKey ('functionalBaseUrl')) {
            String baseUrl = buildBinding.grailsSettings.functionalTestBaseUrl
            variables.put ('functionalBaseUrl', baseUrl)
            variables.put ('functionalTestBaseUrl', baseUrl)
        }
    }

    private boolean isForked () {
        // todo should be test, but it is wrong in grails 2.3.4
        buildBinding.variables.buildSettings?.forkSettings?.run
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
