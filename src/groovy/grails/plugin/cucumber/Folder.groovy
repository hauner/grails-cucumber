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


class Folder {
    private File file

    Folder (File file) {
        this.file = file
    }

    def path () {
        file.canonicalPath
    }

    def exists () {
        file.exists ()
    }

    def create () {
        file.mkdirs ()
    }

    def isEmpty () {
        file.list ().length == 0
    }
}

