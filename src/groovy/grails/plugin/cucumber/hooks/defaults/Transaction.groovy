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

package grails.plugin.cucumber.hooks.defaults

import org.codehaus.groovy.grails.test.support.GrailsTestTransactionInterceptor

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)


Before ("~@notxn") {
    println "before transaction"
    transactionInterceptor = new GrailsTestTransactionInterceptor (appCtx)
    transactionInterceptor.init ()
}

After ("~@notxn") {
    transactionInterceptor.destroy ()
    println "after transaction"
}
