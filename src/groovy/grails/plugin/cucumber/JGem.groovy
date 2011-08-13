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


class JGem {
    static final String JGEM_RESOURCE_PATH = '/META-INF/jruby.home/bin/jgem'
    final String jgem

    JGem () {
        jgem = JGEM_RESOURCE_PATH
    }

    JGem (String jGemPath) {
        jgem = jGemPath
    }

    Reader reader () {
        InputStream stream = getClass().getResourceAsStream (name ())

        if (stream == null) {
            throw new FileNotFoundException (name ())
        }

        new InputStreamReader (stream)
    }

    String name () {
        jgem
    }
}
