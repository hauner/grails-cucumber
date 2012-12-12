/*
 * Copyright 2012 Martin Hauner
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

import static cucumber.api.groovy.Hooks.After
import static cucumber.api.groovy.Hooks.Before

import org.codehaus.groovy.grails.test.support.GrailsTestTransactionInterceptor
import org.codehaus.groovy.grails.test.support.GrailsTestRequestEnvironmentInterceptor



class Hooks {
    static void hooks (Closure body) {
        def binding = binding (body)
        def delegate = new HookDSL (binding.appCtx)

        body.setDelegate (delegate)
        body.setResolveStrategy(Closure.DELEGATE_ONLY)

        body ()
    }

    private static Binding binding (Closure body) {
        body.delegate.binding
    }

    static class HookDSL {
        def appCtx
        def transactionInterceptor
        def requestInterceptor

        HookDSL (def appCtx) {
             this.appCtx = appCtx
        }

        def integration (Object... tags) {
            addBeforeHook (tags, beforeTransaction)
            addAfterHook (tags, afterTransaction)
            addBeforeHook (tags, beforeRequest)
            addAfterHook (tags, afterRequest)
        }

        def transaction (Object... tags) {
            addBeforeHook (tags, beforeTransaction)
            addAfterHook (tags, afterTransaction)
        }

        def request (Object... tags) {
            addBeforeHook (tags, beforeRequest)
            addAfterHook (tags, afterRequest)
        }

        def addBeforeHook (def tags, Closure code) {
            def varargs = tags.toList ()
            varargs << code
            Before (varargs.toArray ())
        }

        def addAfterHook (def tags, Closure code) {
            def varargs = tags.toList()
            varargs << code
            After (varargs.toArray ())
        }

        /**
         * grails transaction handling for integration tests
         */
        def beforeTransaction = {
            //println "before transaction"
            transactionInterceptor = new GrailsTestTransactionInterceptor (appCtx)
            transactionInterceptor.init ()
        }

        def afterTransaction = {
            transactionInterceptor.destroy ()
            //println "after transaction"
        }

        /**
         * grails request handling for integration tests
         */
        def beforeRequest = {
            //println "before request"
            requestInterceptor = new GrailsTestRequestEnvironmentInterceptor (appCtx)
            requestInterceptor.init ()
        }

        def afterRequest = {
            requestInterceptor.destroy ()
            //println "after request"
        }
    }
}
