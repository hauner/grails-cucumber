package pages

import geb.Page


class ElementShowPage extends Page {
    static at = {
        title ==~ /Show Element/
    }

    static content = {
        listElement (to: ElementListPage) {
            $ ('span.menuButton a.list')
        }

        row { val ->
            $ ('td.name', text: val).parent ()
        }

        value { val ->
            row (val).find ('td.value').text ()
        }

        id {
            value ('Id')
        }

        name {
            value ('Name')
        }

        symbol {
            value ('Symbol')
        }

        atomicNumber {
            value ('Atomic Number')
        }
    }

    def checkElement (String element) {
        if (element == "Gold") {
            assert id.number
            assert name == 'Gold'
            assert symbol == 'Au'
        }
        else {
            assert false
        }
    }
}
