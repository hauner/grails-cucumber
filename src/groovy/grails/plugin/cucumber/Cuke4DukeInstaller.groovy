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


class Cuke4DukeInstaller {
    String pluginDir

	Cuke4DukeInstaller () {
        println "** Cuke4DukeInstaller.new ()"
        //this.pluginDir = pluginDir
    }

	Cuke4DukeInstaller (pluginDir) {
        println "** Cuke4DukeInstaller.new (pluginDir)"
        //this.pluginDir = pluginDir
    }

    void run () {
        println "** Cuke4DukeInstaller.run () : ${pluginDir}"
    }
}

