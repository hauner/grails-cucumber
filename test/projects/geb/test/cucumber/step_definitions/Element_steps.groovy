import pages.ElementListPage
import pages.ElementNewPage
import pages.ElementShowPage

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin (cucumber.runtime.groovy.EN)


Given (~'^\"([^\"]*)\" already exists$') { String element ->
    ElementFactory.createElement (element)
}

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

    page.checkElementAtRow (element, 0)
}
