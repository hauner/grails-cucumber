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

import pages.ElementListPage
import pages.ElementNewPage
import pages.ElementShowPage
import grails.plugin.cucumber.Element

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin (cucumber.runtime.groovy.EN)


/*
def createElement (String element) {
    println "before save"
    if (element == 'Gold') {
        //new Element (atomicNumber: 79, name: 'Gold', symbol: 'Au').save ()
    }
    println "after save"
}
*/


//Given (~'^\"([^\"]*)\" already exists$') { String element ->
    //createElement (element)
//}

Given (~'^I am in my laboratory$') { ->
    to ElementListPage
    assert at (ElementListPage)
}

When (~'^I mix the secret ingredients of \"([^\"]*)\"$') { String element ->
    page.newElement.click ()
    assert at (ElementNewPage)

    page.enterElement (element)
}

Then (~'^I get \"([^\"]*)\" as a new element$') { String element ->
    assert at (ElementShowPage)

    page.checkElement (element)
}

Then (~'^my element list contains \"([^\"]*)\"$') { String element ->
    to ElementListPage
    assert at (ElementListPage)

    page.checkElement (element, 0)
}
