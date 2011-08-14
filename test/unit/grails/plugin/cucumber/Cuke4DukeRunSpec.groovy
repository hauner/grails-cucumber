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

import org.jruby.embed.ScriptingContainer
import grails.plugin.spock.*


class Cuke4DukeRunSpec extends UnitSpec {

    def "run cuke4duke" () {
        given:
        def home = Mock (Folder)
        _ * home.path () >> "HomePath"

        def reader = new StringReader ("cuke4duke = true")
        def cuke = Mock (Cuke4Duke)
        _ * cuke.reader () >> reader
        _ * cuke.name () >> "cuke4duke"

        def container = Mock (ScriptingContainer)
        def factory = Mock (JRubyFactory)
        _ * factory.container () >> container

        def cuketestpath = "test/cucumber"
        def runner = new JRubyRunner (factory)
        def run = new Cuke4DukeRun (runner, home, cuke, cuketestpath)

        when:
        run.run ()

        then:
        1 * container.setArgv ([
            "test/cucumber"
        ] as String[])

        then:
        1 * container.runScriptlet ( {"$it".contains ("GEM_PATH")} )

        then:
        1 * container.runScriptlet (cuke.reader (), cuke.name ())
    }

}
