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


// Spock can't stub/mock the additional groovy methods of File
class MockableFile extends File {
    BufferedInputStream newInputStream () {
        return null
    }
}

class Cuke4DukeSpec extends UnitSpec {

    def "creates reader for cuke4duke" () {
        given:
        File file = Mock (MockableFile)
        _ * file.newInputStream () >> Mock (BufferedInputStream)
        def cuke = new Cuke4Duke (file)

        when:
        def reader = cuke.reader ()

        then:
        reader != null
    }

    def "throws when it can't find cuke4duke" () {
        given:
        // simulate bad classpath
        def badpath = "/NotInClassPath/cuke4duke"
        def badcuke = new File (badpath)
        def cuke = new Cuke4Duke (badcuke)

        when:
        def reader = cuke.reader ()

        then:
        FileNotFoundException e = thrown ()
        e.message.contains (badpath)
    }
}
