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


class CucumberGrailsTestType extends GrailsTestTypeSupport {
    static final NAME = "cucumber"
    String pluginHome
    String basedir

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

    void setup () {
        def jrubyHome = new Folder (new File (homepath ()))
        def runner = new JRubyRunner (new JRubyFactory ())
        def installer = new Cuke4DukeInstaller (runner, jrubyHome, new JGem ())
        // todo switch parameter
        def setup = new Cuke4DukeSetup (jrubyHome, installer)
        setup.run ()
    }

    @Override
    int doPrepare () {
        def cuke = new Cuke4Duke (new File (cukebinpath ()))
        def runner = new JRubyRunner (new JRubyFactory ())
        def prepare = new Cuke4DukePrepare (runner, cuke, featurepath ())
        prepare.run ()
    }

    @Override
    GrailsTestTypeResult doRun (GrailsTestEventPublisher eventPublisher) {
        //eventPublisher.testCaseStart('*** Cucumber Test Case Start ***')
        //eventPublisher.testStart('** Cucumber Test Start **')
        //eventPublisher.testEnd('** Cucumber Test End **')
        //eventPublisher.testCaseEnd('*** Cucumber Test Case End ***')
        return new CucumberGrailsTestTypeResult ()
    }
}
