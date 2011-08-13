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

import grails.plugin.spock.*


class Cuke4DukeSetupSpec extends UnitSpec {

    def "creates jruby home if it does not exists" () {
        def home = Mock (Folder)
        def installer = Mock (Cuke4DukeInstaller)
        def setup = new Cuke4DukeSetup (installer, home)

        when:
        setup.run ()

        then:
        1 * home.exists () >> false
        1 * home.create () >> true
    }

    def "installs cuke4duke if jruby home is empty" () {
        def home = Mock (Folder)
        1 * home.isEmpty () >> true
        def installer = Mock (Cuke4DukeInstaller)
        def setup = new Cuke4DukeSetup (installer, home)

        when:
        setup.run ()

        then:
        1 * installer.run ()
    }

    def "skips installation of cuke4duke if jruby home is not empty" () {
        def home = Mock (Folder)
        1 * home.isEmpty () >> false
        def installer = Mock (Cuke4DukeInstaller)
        def setup = new Cuke4DukeSetup (installer, home)

        when:
        setup.run ()

        then:
        0 * installer.run ()
    }
}
