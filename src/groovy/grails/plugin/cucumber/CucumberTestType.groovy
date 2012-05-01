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


import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import grails.util.Environment
import cucumber.runtime.Runtime
import cucumber.runtime.groovy.GroovyBackend
import cucumber.runtime.snippets.SummaryPrinter
import grails.plugin.cucumber.io.FileResourceLoader
import grails.plugin.cucumber.hooks.defaults.Transaction


class CucumberTestType extends GrailsTestTypeSupport {
    static final ENVIRONMENT = Environment.TEST.name
    static final CONFIG_NAME = "CucumberConfig.groovy"
    static final CONFIG_PATH = ["grails-app", "conf", CONFIG_NAME].join (File.separator)
    static final NAME = "cucumber"

    GroovyShell grailsShell
    String baseDir

    Cucumber cucumber

    CucumberTestType (String testPhase, String baseDir, GroovyShell grailsShell) {
        super (NAME, testPhase)
        this.grailsShell = grailsShell
        this.baseDir = baseDir
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
        def configReader = new ConfigReader (new File (CONFIG_PATH),new ConfigSlurper (ENVIRONMENT))
        def configObject = configReader.parse ()
        configObject.cucumber.defaultFeaturePath = featurePath ()
        configObject.cucumber.defaultGluePath = featurePath ()
        configObject.cucumber.fixedGluePath = Transaction.class.package.name

        //def resourceLoader = new FileResourceLoader ()
        def resourceLoader = new FileResourceLoader (new FileFilter() {
            boolean accept (File file) {
                file.name != CONFIG_NAME
            }
        })
        def classLoader = getTestClassLoader ()
        def groovyShell = new GroovyShell (classLoader, createBinding ())
        def groovyBackend = new GroovyBackend (groovyShell, resourceLoader)

        def summaryPrinter = new SummaryPrinter (System.out)
        def runtimeOptions = new RuntimeOptionsBuilder (configObject).build ()
        def runtime = new Runtime (resourceLoader, classLoader, [groovyBackend], runtimeOptions)

        cucumber = new Cucumber (resourceLoader, runtime, runtimeOptions, summaryPrinter)
    }

    private Binding createBinding () {
        Map variables = grailsShell.context.variables.clone () as Map
        variables.remove ("metaClass")
        variables.remove ("getMetaClass")
        variables.remove ("setMetaClass")
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
