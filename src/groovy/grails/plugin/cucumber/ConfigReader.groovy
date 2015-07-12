/*
 * Copyright 2012-2013, 2015 Martin Hauner
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


class ConfigReader {
    ConfigSlurper configSlurper
    File configFile

    ConfigReader (File configFile, ConfigSlurper configSlurper) {
        this.configFile = configFile
        this.configSlurper = configSlurper
    }

    ConfigObject parse () {
        if (exists ()) {
            configSlurper.parse (configFile.toURI ().toURL () as URL)
        }
        else {
            new ConfigObject ()
        }
    }

    boolean exists () {
        configFile.exists ()
    }
}
