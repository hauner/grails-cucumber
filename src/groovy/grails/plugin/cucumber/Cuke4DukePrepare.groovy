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


class Cuke4DukePrepare {
    JRubyRunner runner
    Folder home
    Cuke4Duke cuke4duke
    String featurepath

    Cuke4DukePrepare (JRubyRunner runner, Folder home, Cuke4Duke cuke4duke,
        String featurepath) {
        this.runner = runner
        this.home = home
        this.cuke4duke = cuke4duke
        this.featurepath = featurepath
    }

    int run () {
        runner.run { jruby ->
            jruby.setArgv ([
                //"--help"
                "--dry-run",
                //"--quiet",
                //"format",
                //"progress",
                //"--no-color",
                featurepath
            ] as String[])

            jruby.runScriptlet ("ENV['GEM_PATH'] = \"${home.path ()}\"")
            jruby.runScriptlet (cuke4duke.reader (), cuke4duke.name ())
        }
        // todo result count from custom formatter
        1
    }
	
}
