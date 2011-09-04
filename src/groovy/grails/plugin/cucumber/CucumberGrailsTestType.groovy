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
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.junit4.listener.SuiteRunListener
import org.codehaus.groovy.grails.test.junit4.result.JUnit4ResultGrailsTestTypeResultAdapter
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper

import cucumber.resources.FileResource
import cucumber.resources.Resource
import cucumber.resources.Resources
import cucumber.resources.Consumer
import cucumber.runtime.FeatureBuilder
import cucumber.runtime.SnippetPrinter
import cucumber.runtime.model.CucumberFeature
import gherkin.formatter.Formatter
import gherkin.formatter.Reporter
import gherkin.formatter.PrettyFormatter
import gherkin.formatter.model.*
import cucumber.runtime.Runtime
import cucumber.runtime.groovy.GroovyBackend
import gherkin.GherkinParser
import org.junit.runner.Result
import org.junit.runner.Describable
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runner.notification.Failure
import org.junit.runners.Suite
import junit.framework.Test
import junit.framework.TestResult
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest

//import org.codehaus.groovy.grails.test.junit4.runner.GrailsTestCaseRunnerBuilder

//import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
//import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
//import org.codehaus.groovy.grails.test.support.GrailsTestMode
//import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher

//import org.junit.runners.Suite
//import org.junit.runner.notification.RunNotifier


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class MyRunListener extends RunListener {

    private final GrailsTestEventPublisher eventPublisher
    private final JUnitReportsFactory reportsFactory
    private final SystemOutAndErrSwapper outAndErrSwapper

    private startTime  // changing state
    private testSuite
    
    private report
    private name

     MyRunListener (GrailsTestEventPublisher eventPublisher, JUnitReportsFactory
         reportsFactory, SystemOutAndErrSwapper outAndErrSwapper) {
        this.eventPublisher = eventPublisher
        this.reportsFactory = reportsFactory
        this.outAndErrSwapper = outAndErrSwapper
    }

    void start (String scenarioName) {
        name = scenarioName

        eventPublisher.testCaseStart (name)
        testSuite = new JUnitTest (name)

        report = reportsFactory.createReports (name)
        outAndErrSwapper.swapIn()
        reports.startTestSuite(testSuite)
        startTime = System.currentTimeMillis()
    }

    void finish () {
        testSuite.runTime = System.currentTimeMillis() - startTime
        testSuite.setCounts(runCount, failureCount, errorCount)
        def (out, err) = outAndErrSwapper.swapOut()*.toString()
        reports.systemOutput = out
        reports.systemError = err
        reports.endTestSuite(testSuite)
        eventPublisher.testCaseEnd(name)
    }

    @Override
    public void testRunStarted (Description description) throws Exception {
        println "testRunStarted: " + description
    }

    @Override
    public void testRunFinished (Result result) throws Exception {
        println "testRunFinished: " + result.getRunCount ()
    }

    @Override
    public void testStarted(Description description) throws Exception {
        println "testStarted: " + description


        def testName = description.methodName
        eventPublisher.testStart(testName)
        //runCount++
        [System.out, System.err]*.println("--Output from ${testName}--")
        reports.startTest(getTest(description))
    }

    @Override
    public void testFinished(Description description) throws Exception {
        println "testFinished: " + description
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        println "testFailure: " + failure
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        println "testAssumptionFailure: " + failure
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        println "testIgnored: " + description
    }
}

/*
class CucumberSuite extends Suite {
    CucumberSuite (List<ScenarioRunner> runners) {
        super (null, runners)
    }
}
*/

class CucumberTest implements Test, Describable {
    String description

    CucumberTest () {
        System.out << "CT(CucumberTest)\n"
        this.description = "CT(DESCRIPTION)"
    }

    CucumberTest (description) {
        System.out << "CT(CucumberTest(description))\n"
        this.description = description
    }

	int countTestCases () {
        System.out << "CT(countTestCases)\n"
        return 1
    }

	void run(TestResult result) {
        throw new RuntimeException ("this test can not be run, it is just informational!");
    }

    @Override
    String toString () {
        System.out << "CT(toString)\n"
        return "CucumberTestToString"
    }

	Description getDescription () {
        System.out << "CT(getDescription)\n"
        //return Description.createSuiteDescription ("HUHUHU")
        return Description.createTestDescription(getClass (), "HUHUHUHH")
    }
}

class DebugFormatter implements Formatter, Reporter {
    def sysout
    def pretty

    DebugFormatter (sysout, pretty) {
        this.sysout = sysout
        this.pretty = pretty
    }

    void uri (String uri) {
        sysout << "\nF(uri): ($uri)\n"

        pretty.uri (uri)
    }

    void feature (Feature feature) {
        sysout << "F(feature): (${feature.getName ()})\n"

        pretty.feature (feature)
    }

    void background (Background background) {
        //sysout << "F(background): ($background.getName ())\n"
    }

    void scenario (Scenario scenario) {
        sysout << "F(scenario): (${scenario.getName ()})\n"

        pretty.scenario (scenario)
    }

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        //sysout << "F(scenarioOutline): (${scenarioOutline.getName ()}})\n"
    }

    void examples (Examples examples) {
        //sysout << "F(examples): (${examples.getName ()}})\n"
    }

    void step (Step step) {
        //sysout << "F(step): (${step.getName ()}})\n"
    }

    void eof () {
        //sysout << "F(eof)\n"
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, int line) {
        //sysout << "F(syntaxError)\n"
    }


    /*
     * Reporter
     */

    void result (gherkin.formatter.model.Result result) {
        sysout << "R(result):\n"
    }

    void match (Match match) {
        sysout << "R(match)\n"

        pretty.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        sysout << "R(embedding):\n"
    }
}


class GherkinFormatter implements Formatter, Reporter {
    Formatter formatter
    GrailsTestEventPublisher eventPublisher
    JUnitReportsFactory reportsFactory
    SystemOutAndErrSwapper outAndErrSwapper

    def sysout

    Feature activeFeature = null
    Scenario activeScenario = null
    Step activeStep = null

    /*TestSuite*/def testSuite
    def test
    /*JUnitReports*/def  reports
    def startTime
    def uri

    def runCount = 0
    def failureCount = 0
    def errorCount = 0

    GherkinFormatter (
        Formatter formatter,
        GrailsTestEventPublisher eventPublisher,
        JUnitReportsFactory reportsFactory,
        SystemOutAndErrSwapper outAndErrSwapper) {
        this.formatter = formatter
        this.eventPublisher = eventPublisher
        this.reportsFactory = reportsFactory
        this.outAndErrSwapper = outAndErrSwapper
        this.sysout = System.out
    }


    CucumberTestTypeResult getResult () {
        new CucumberTestTypeResult (runCount, failureCount, errorCount)
    }

    void uri (String uri) {
        sysout << "GF(uri): ($uri)\n"
        this.uri = uri
    }


    private void startFeature () {
        sysout << "GF(startFeature): (${activeFeature.getName ()})\n"

        eventPublisher.testCaseStart (activeFeature.getName ())

        outAndErrSwapper.swapIn()

        //def pretty = new PrettyFormatter (sysout/*System.out*/, true, true)
        formatter = new PrettyFormatter (System.out, true, true)
        //formatter = new DebugFormatter (sysout, pretty)

        reports = reportsFactory.createReports (activeFeature.getName ())

        testSuite = new JUnitTest (activeFeature.getName ())
        reports.startTestSuite (testSuite)

        startTime = System.currentTimeMillis()
    }
    
    private void finishFeature () {
        sysout << "GF(finishFeature): (${activeFeature.getName ()})\n"

        testSuite.runTime = System.currentTimeMillis() - startTime

        testSuite.setCounts (runCount, failureCount, errorCount)

        def (out, err) = outAndErrSwapper.swapOut ()*.toString ()
        reports.systemOutput = out
        reports.systemError = err
        reports.endTestSuite (testSuite)

        eventPublisher.testCaseEnd (activeFeature.getName ())
        sysout << "Counts: $runCount, $failureCount, $errorCount\n"
    }

    void finish () {
        finishScenario ()
        finishFeature ()
    }

    void feature (Feature feature) {
        //sysout << "GF(feature): (${feature.getName ()})\n"

        // finish previous feature
        if (activeFeature != null) {
            finishFeature ()
        }

        // start next feature
        activeFeature = feature
        startFeature ()

        // normal cucumber output
        formatter.uri (uri)
        formatter.feature (feature)
    }

    void background (Background background) {
        sysout << "GF(background): ($background)\n"

        formatter.background (background)
    }

    private void startScenario () {
        sysout << "\nGF(startScenario): (${activeScenario.getName ()})\n"

        eventPublisher.testStart (activeScenario.getName ())

        runCount++
        [System.out, System.err]*.println ("\n--Output from ${activeScenario.getName ()}--")

        test = new CucumberTest ()
        reports.startTest (test)
    }

    private void finishScenario () {
        sysout << "GF(finishScenario): (${activeScenario.getName ()})\n"

        reports.endTest (test)
        eventPublisher.testEnd (activeScenario.getName ())
    }

    void scenario (Scenario scenario) {

        if (activeScenario != null) {
            finishScenario ()
        }

        activeScenario = scenario
        startScenario ()

        formatter.scenario (scenario)
    }
    //eventPublisher.testFailure ()

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        sysout << "GF(scenarioOutline): (${scenarioOutline.getName ()}})\n"

        formatter.scenarioOutline (scenarioOutline)
    }

    void examples (Examples examples) {
        sysout << "GF(examples): (${examples.getName ()}})\n"

        formatter.examples (examples)
    }

    void step (Step step) {
        sysout << "GF(step): (${step.getName ()}})\n"

        activeStep = step
        formatter.step (step)
    }

    void eof () {
        sysout << "GF(eof)\n"

        formatter.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, int line) {
        sysout << "GF(syntaxError)\n"

        formatter.syntaxError (state, event, legalEvents, uri, line)
    }


    /*
     * Reporter
     */

    // for each step
    void result (gherkin.formatter.model.Result result) {
        sysout << "GR(result):\n"
        //sysout << result.getStatus() + "\n"

        if (result.getErrorMessage() != null) {
            def exception = result.getError ()

            sysout << exception
            if (exception instanceof AssertionError) {
                sysout << "GR(result): failure\n"

                failureCount++
                reports.addFailure (test, exception)

                eventPublisher.testFailure (activeStep.getName (), exception)
            }
            else {
                sysout << "GR(result): error\n"

                errorCount++
                reports.addError (test, exception)

                eventPublisher.testFailure (activeStep.getName (), exception, true)
            }
        }
        else {
            sysout << "GR(result): success\n"
            //????
            //reports.endTest (test)
            //eventPublisher.testEnd (activeScenario.getName ())
        }

        formatter.result (result)
    }

    void match (Match match) {
        sysout << "GR(match):\n"

        formatter.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        sysout << "GR(embedding):\n" //+ mimeType +" / "+ data
    }
}





class GherkinReporter implements Reporter {
  GherkinReporter () {
  }

  void result (gherkin.formatter.model.Result result) {
    println "GR(result): " + result
  }

  void match (Match match) {
    println "GR(match): " + match
  }

  void embedding (String mimeType, byte[] data) {
    println "GR(embedding): " + mimeType +" / "+ data
  }
}


class CucumberTestTypeResult implements GrailsTestTypeResult {
    long runCount
    long failureCount
    long errorCount

    CucumberTestTypeResult (long runCount, long failureCount, long errorCount) {
        this.runCount = runCount
        this.failureCount = failureCount
        this.errorCount = errorCount
    }

    int getPassCount () {
        runCount - getFailCount ()
    }

    int getFailCount () {
        failureCount + errorCount
    }
}



class CucumberGrailsTestType extends GrailsTestTypeSupport {
    static final NAME = "cucumber"
    String pluginHome
    String basedir

    Runtime runtime
    List<CucumberFeature> cucumberFeatures
    List<Object> filters


    CucumberGrailsTestType (String pluginHome, String basedir) {
        super (NAME, NAME)
        this.pluginHome = pluginHome
        this.basedir = basedir
    }

    String homepath () {
        [pluginHome, "lib", ".jruby"].join (File.separator)
    }

    String cukebinpath () {
        [homepath (), "bin", "cuke4duke"].join (File.separator)
    }

    String featurepath () {
        [basedir, "test", relativeSourcePath].join (File.separator)
    }

    // steps resource / implementation
    String testpath () {
        ["test", relativeSourcePath].join (File.separator)
    }

    String resources () {
        [basedir, "resources"].join (File.separator)
    }

    String featurepath2 () {
        featurepath () + File.separator
    }

    List<String> getTestExtensions () {
        ["feature"]
    }

    // obsolete
    /*
    void setup () {
        def jrubyHome = new Folder (new File (homepath ()))
        def runner = new JRubyRunner (new JRubyFactory ())
        def installer = new Cuke4DukeInstaller (runner, jrubyHome, new JGem ())
        def setup = new Cuke4DukeSetup (installer, jrubyHome)
        setup.run ()
    }
    */

    @Override
    int doPrepare () {
        def ccl = Thread.currentThread ().getContextClassLoader ()
        println "CCL $ccl"
        def pcl = ccl.getParent ()
        println "PCL $pcl"

        //ccl.addURL (new File (featurepath ()).toURI ().toURL ())
        //ccl.addURL (new File (testpath ()).toURI ().toURL ())
        ccl.addURL (new File (basedir).toURI ().toURL ())
        ccl.URLs.each { println "ccp: $it" }
        //pcl.addURL (new File (featurepath ()).toURI ().toURL ())
        // rootLoader
        //getClass ().classLoader.rootLoader.addURL (new File (featurepath ()).toURI ().toURL ())
        //getClass ().classLoader.rootLoader.addURL (new File (stepspath ()).toURI ().toURL ())
        //pcl.URLs.each { println "pcp: $it" }
/*
        def jar = "/Users/hauner/grails-1.3.7/lib/gant_groovy1.7-1.9.2.jar"
        def dir = "/Users/hauner/Development/Grails/grails-cucumber.git/target/classes"
 
        println "URLS >>"
        //def tmp = "CucumberGrailsPlugin.class" // works!!
        //def tmp = "cucumber" // works!!!!
        //def tmp = "test/cucumber" // works!!!!
        def tmp = testpath () // works!!!!
        Enumeration<URL> resources = ccl.getResources (tmp)
        if (!resources.hasMoreElements()) {
            println "no elements"
        }
        else {
            ccl.getResources (tmp).each { println "urls: $it" }
        }
        //pcl.getResources ("test/cucumber").each { println "target pcl urls: $it" }
        //ccl.getResources (resources () + "/").each { println "ccl urls: $it" }
        //pcl.getResources (resources () + "/").each { println "pcl urls: $it" }
        println "URLS <<"
*/

        runtime = new Runtime (new GroovyBackend (testpath ()))

        cucumberFeatures = new ArrayList<CucumberFeature> ();
        FeatureBuilder builder = new FeatureBuilder (cucumberFeatures);
        filters = new ArrayList ()

        // load gherkin files //new File (featurepath ())
        Resources.scan (testpath (), ".feature",
          new Consumer() {
            @Override
            public void consume (Resource resource) {
              builder.parse (resource, filters)
            }
          }
        )

      /*
            private List<CucumberFeature> load(List<String> filesOrDirs, final List<Object> filters) {
        final List<CucumberFeature> cucumberFeatures = new ArrayList<CucumberFeature>();
        final FeatureBuilder builder = new FeatureBuilder(cucumberFeatures);
        for (String fileOrDir : filesOrDirs) {
            Resources.scan(fileOrDir, ".feature", new Consumer() {
                @Override
                public void consume(Resource resource) {
                    builder.parse(resource, filters);
                }
            });
        }
        return cucumberFeatures;
    }
    */


        // cuke4duke crashes with --dry-run!?
        /*
        def jrubyHome = new Folder (new File (homepath ()))
        def cuke = new Cuke4Duke (new File (cukebinpath ()))
        def runner = new JRubyRunner (new JRubyFactory ())
        def prepare = new Cuke4DukePrepare (runner, jrubyHome, cuke, featurepath ())
        prepare.run ()
        *///1
        def scenarioCount = 0
        cucumberFeatures.each {
            scenarioCount += it.getCucumberScenarios ().size ()
        }
       //cucumberFeatures.size ()
       scenarioCount
    }

    /*
    boolean isFeature (File file) {
        file.getName ().endsWith (".feature")
    }

    boolean isDir (File file) {
        file.isDirectory ()
    }

    void findChildFeatures (File parent) {
        File[] children = parent.listFiles ()
        for (File child : children) {
            findFeatures (child)
        }
    }

    void findFeatures (File root) {
        if (isDir (root)) {
            findChildFeatures (root)
        }
        else if (isFeature (root)) {
            addFeature (root)
        }
    }

    void addFeature (File feature) {
        println "$feature"

        RunnerBuilder builder = new RunnerBuilder (runtime, scenarioRunners)
        GherkinParser gherkinParser = new GherkinParser (builder)

        String source = new FileResource (null, feature).getString ()
        gherkinParser.parse (source, feature.getAbsolutePath (), 0)
    }
    */

    def createJUnitReportsFactory () {
        JUnitReportsFactory.createFromBuildBinding (buildBinding)
    }

    def createListener (eventPublisher) {

        new SuiteRunListener (eventPublisher,
            createJUnitReportsFactory (), createSystemOutAndErrSwapper ())
    }

    def createNotifier (eventPublisher) {
        RunNotifier notifier = new RunNotifier ()
        notifier.addListener (createListener (eventPublisher))
        notifier
    }

    @Override
    GrailsTestTypeResult doRun (GrailsTestEventPublisher eventPublisher) {
        def reportsFactory = createJUnitReportsFactory ()
        def outAndErrSwapper = createSystemOutAndErrSwapper ()

        //def result = new CucumberGrailsTestTypeResult ()
        //def result = new Result ()
        //def listener = new MyRunListener (eventPublisher, reportsFactory, outAndErrSwapper)
        //RunNotifier notifier = new RunNotifier ()
        //notifier.addListener (new CucumberRunListener ())

        //RunNotifier notifier = new RunNotifier ()
        //RunNotifier notifier = createNotifier (eventPublisher)
        //notifier.addListener (result.createListener ())
        //notifier.addListener (listener)

              //      perTestListener = new PerTestRunListener(testName, eventPublisher, reportsFactory.createReports(testName), outAndErrSwapper)
        //Suite suite = new CucumberSuite (scenarioRunners)
        //suite.run (notifier)
        //

        // TODO eigener SuiteRunListener der eine report mit scenario namen
        // alengt und ein PerStepListener der

        //def pretty = new PrettyFormatter (System.out, true, true)
        def formatter = new GherkinFormatter (null, eventPublisher, reportsFactory, outAndErrSwapper)
        def reporter = new GherkinReporter ()

        /*
        def runCount = 0
        def failureCount = 0
        def errorCount = 0

        def featureName
        def testSuite
        def reports
        def startTime
        */

        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            /*
            def name = cucumberFeature.getFeature ().getName ()

            if (featureName != name) {
                if (featureName != null) {
                    // finish
                    testSuite.runTime = System.currentTimeMillis() - startTime
                    testSuite.setCounts (runCount, failureCount, errorCount)
                    def (out, err) = outAndErrSwapper.swapOut ()*.toString ()
                    reports.systemOutput = out
                    reports.systemError = err
                    reports.endTestSuite (testSuite)
                    eventPublisher.testCaseEnd (name)
                    println "Counts: $runCount, $failureCount, $errorCount"
                }

                // start
                eventPublisher.testCaseStart (name)
                testSuite = new JUnitTest (name)
                outAndErrSwapper.swapIn()
                reports = reportsFactory.createReports (name)
                reports.startTestSuite(testSuite)
                startTime = System.currentTimeMillis()
            }
            */

            //listener.start (cucumberFeature.getFeature ().getName ())
            //def pretty = new PrettyFormatter (System.out, true, true)
             //def formatter = GherkinFormatter (pretty, eventPublisher, null, null)

            cucumberFeature.run (runtime, formatter, formatter/*reporter*/)
            //listener.finish ()

        }

        formatter.finish ()
        /*
        // finish last iteration
        if (name != null) {
            // finish
            testSuite.runTime = System.currentTimeMillis() - startTime
            testSuite.setCounts (runCount, failureCount, errorCount)
            def (out, err) = outAndErrSwapper.swapOut ()*.toString ()
            reports.systemOutput = out
            reports.systemError = err
            reports.endTestSuite (testSuite)
            eventPublisher.testCaseEnd (featureName)
        }
*/

        new SnippetPrinter (System.out).printSnippets (runtime)
        /*
         *runtime.getSnippets ().each {
            println "snippet $it"
        }
       */

        //notifier.fireTestRunFinished (result)
        //new JUnit4ResultGrailsTestTypeResultAdapter (result)
        formatter.getResult ()

        /*
    void start (String scenarioName) {
        name = scenarioName

        eventPublisher.testCaseStart (name)
        testSuite = new JUnitTest (name)

        report = reportsFactory.createReports (name)
        outAndErrSwapper.swapIn()
        reports.startTestSuite(testSuite)
        startTime = System.currentTimeMillis()
    }

    void finish () {
        testSuite.runTime = System.currentTimeMillis() - startTime
        testSuite.setCounts(runCount, failureCount, errorCount)
        def (out, err) = outAndErrSwapper.swapOut()*.toString()
        reports.systemOutput = out
        reports.systemError = err
        reports.endTestSuite(testSuite)
        eventPublisher.testCaseEnd(name)
    }
     */


        /* obsolete
        def formatter = new Cuke4DukeFormatter (eventPublisher, result)

        def jrubyHome = new Folder (new File (homepath ()))
        def cuke = new Cuke4Duke (new File (cukebinpath ()))
        def runner = new JRubyRunner (new JRubyFactory ())
        def run = new Cuke4DukeRun (runner, jrubyHome, cuke, featurepath ())
        run.formatter = formatter // todo pass to constructor
        run.run ()
        */
        //eventPublisher.testCaseStart('*** Cucumber Test Case Start ***')
        //eventPublisher.testStart('** Cucumber Test Start **')
        //eventPublisher.testEnd('** Cucumber Test End **')
        //eventPublisher.testCaseEnd('*** Cucumber Test Case End ***')
        //result
    }
}
