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


class FolderSpec extends UnitSpec {

    def "path is canonical" () {
        def path = ["root", "lib", ".jruby"].join (File.separator)
        File file = new File (path)

        when:
        def home = new Folder (file)

        then:
        home.path () == file.canonicalPath
    }

    def "checks if path exists" () {
        def file = Mock (File)

        when:
        def home = new Folder (file)
        def exists = home.exists ()

        then:
        1 * file.exists () >> true
        exists == true
    }

    def "creates path" () {
        def file = Mock (File)

        when:
        def home = new Folder (file)
        def created = home.create ()

        then:
        1 * file.mkdirs () >> true
        created == true
    }

    def "checks is empty" () {
        def file = Mock (File)
        def childs = new String[0]

        when:
        def home = new Folder (file)
        def empty = home.isEmpty ()

        then:
        1 * file.list () >> childs
        empty == true
    }
}
