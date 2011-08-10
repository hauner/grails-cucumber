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


class Cuke4DukeInstallerSpec extends UnitSpec {

    def "runs jgem to install cuke4duke" () {
        def home = Mock (Folder)
        _ * home.path () >> "HomePath"
        def jgem = Mock (JGem)
        def installer = new Cuke4DukeInstaller (home, jgem)

        when:
        installer.run ()

        then:
        1 * jgem.run ([
            "install",
            "cuke4duke",
            "--version",
            "0.4.4",
            "--install-dir",
            home.path ()
        ])
    }
}
