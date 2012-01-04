package pages

import geb.Page


class ElementNewPage extends Page {
    static at = {
        title ==~ /Create Element/
    }

    static content = {
        saveElement {
            $ ('input.save')
        }
    }

    def enterElement (String element) {
        if (element == "Gold") {
            $ ("form").atomicNumber   = "79"
            $ ("form").name           = "Gold"
            $ ("form").symbol         = "Au"
        }
        saveElement.click ()
    }
}
