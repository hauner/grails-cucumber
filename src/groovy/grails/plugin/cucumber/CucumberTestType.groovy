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

package grails.plugin.cucumber


import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport


class CucumberTestType extends GrailsTestTypeSupport {
    static final NAME = "cucumber"
    String testClassesDir
    String baseDir

    Cucumber cucumber

    CucumberTestType (String relativeSourcePath, String baseDir, String testClassesDir) {
        super (NAME, relativeSourcePath)
        this.testClassesDir = testClassesDir
        this.baseDir = baseDir
    }

    @Override
    List<String> getTestExtensions () {
        ["feature"]
    }

    @Override
    int doPrepare () {
        prepareClasspath ()
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
    
    private void prepareClasspath () {
        // Cucumber scans the classpath for feature "resources". To find features
        // relative to the basedir we put the basedir on the class path.
        addBaseDirToClasspath ()

        // Cucumber uses GroovyShell to parse the steps. To use geb in the steps
        // we need to add it to GroovyShell class path.
        addTestClassesDirToGroovyShellClasspath ()
    }

    private void addBaseDirToClasspath () {
        def extender = new ClassPathExtender (Thread.currentThread ().contextClassLoader)
        extender.add (baseDirURL ())
        //extender.print (System.out)
    }

    private void addTestClassesDirToGroovyShellClasspath () {
        def shellClassLoader = GroovyShell.class.getClassLoader()
        def extender = new ClassPathExtender (shellClassLoader)
        extender.add (testClassesURL ())
        //extender.print (System.out)
    }

    private void prepareCucumber () {
        cucumber = new Cucumber(featurePath ())
    }

    private void loadFeatures () {
        cucumber.loadFeatures ()
    }

    private int countScenarios () {
        cucumber.countScenarios ()
    }

    private GrailsTestTypeResult runFeatures (def publisher) {
        def formatter = createFormatter (publisher)

        cucumber.run (formatter, formatter)

        // todo merge finish into done!?
        formatter.finish ()
        formatter.done ()

        cucumber.printSummary (System.out)

        formatter.getResult ()
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
        ["test", NAME].join (File.separator)
    }

    private URL baseDirURL () {
        new File (baseDir).toURI ().toURL ()
    }

    private String testClassesPath () {
        [baseDir, testClassesDir, relativeSourcePath].join (File.separator)
    }

    private URL testClassesURL () {
        new File (testClassesPath ()).toURI ().toURL ()
    }
}
