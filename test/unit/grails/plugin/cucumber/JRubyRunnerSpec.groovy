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
import org.jruby.embed.EvalFailedException
import grails.plugin.spock.*


class JRubyRunnerSpec extends UnitSpec {

    def "creates jruby container" () {
        given:
        def factory = Mock (JRubyFactory)
        def runner = new JRubyRunner (factory)

        when:
        runner.run {}

        then:
        1 * factory.container ()
    }

    def "does call closure with container" () {
        given:
        def factory = Mock (JRubyFactory)
        def result = Mock (ScriptingContainer)
        1 * factory.container () >> result
        def runner = new JRubyRunner (factory)
        def  container = null

        when:
        runner.run { jruby ->
            container = jruby
        }

        then:
        container == result
    }

    def "container does run ruby code" () {
        given:
        def runner = new JRubyRunner (new JRubyFactory ())
        def value = "not called"

        when:
        runner.run { jruby ->
            value = jruby.runScriptlet ("result = \"called\"")
        }

        then:
        value == "called"
    }

    def "does catch jruby EvalFailedException" () {
        given:
        def factory = Mock (JRubyFactory)
        def runner = new JRubyRunner (factory)

        when:
        runner.run {
            throw new EvalFailedException (null)
        }

        then:
        notThrown (EvalFailedException)
    }
}
