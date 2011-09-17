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

import cucumber.resources.Consumer
import cucumber.resources.Resource
import cucumber.resources.Resources
import cucumber.runtime.FeatureBuilder
import cucumber.runtime.Runtime
import cucumber.runtime.SnippetPrinter
import cucumber.runtime.groovy.GroovyBackend
import cucumber.runtime.model.CucumberFeature
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport


class CucumberTestType extends GrailsTestTypeSupport {
    static final NAME = "cucumber"
    String basedir

    Runtime               cucumberRuntime
    List<CucumberFeature> cucumberFeatures
    List<Object>          cucumberFilters


    CucumberTestType (String basedir) {
        super (NAME, NAME)
        this.basedir = basedir
    }

    String testpath () {
        ["test", relativeSourcePath].join (File.separator)
    }

    URL basedirURL () {
        new File (basedir).toURI ().toURL ()
    }

    @Override
    List<String> getTestExtensions () {
        ["feature"]
    }

    @Override
    int doPrepare () {
        addBaseDirToClasspath ()

        // setup state
        cucumberRuntime = new Runtime (new GroovyBackend (testpath ()))
        cucumberFeatures = new ArrayList<CucumberFeature> ()
        cucumberFilters = new ArrayList ()

        FeatureBuilder builder = new FeatureBuilder (cucumberFeatures)

        // load gherkin files
        Resources.scan (testpath (), ".feature",
          new Consumer() {
            @Override
            public void consume (Resource resource) {
              builder.parse (resource, cucumberFilters)
            }
          }
        )

        // count scenarios
        def scenarioCount = 0
        cucumberFeatures.each {
            scenarioCount += it.getCucumberScenarios ().size ()
        }
        scenarioCount
    }

    @Override
    GrailsTestTypeResult doRun (GrailsTestEventPublisher eventPublisher) {
        def swapper = createSystemOutAndErrSwapper ()
        def factory =  createJUnitReportsFactory ()

        def report = new FeatureReport (new FeatureReportHelper (swapper, factory))
        def pretty = new PrettyFormatterWrapper (new PrettyFormatterFactory ())

        def formatter = new CucumberFormatter2 (eventPublisher, report, pretty, pretty)
//        def formatter = new CucumberFormatter (eventPublisher, new PrettyFormatterFactory (), factory, swapper)
//        formatter.report = report

        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            cucumberFeature.run (cucumberRuntime, formatter, formatter)
        }
        formatter.finish ()

        new SnippetPrinter (System.out).printSnippets (cucumberRuntime)
        
        formatter.getResult ()
    }

    private void addBaseDirToClasspath () {
        def extender = new ClassPathExtender (Thread.currentThread ().contextClassLoader)
        extender.add (basedirURL ())
        //extender.print (System.out)
    }

    private JUnitReportsFactory createJUnitReportsFactory () {
        JUnitReportsFactory.createFromBuildBinding (buildBinding)
    }

}
