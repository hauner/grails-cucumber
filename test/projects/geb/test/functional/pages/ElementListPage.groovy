package pages

import geb.Page
import modules.ElementRow


class ElementListPage extends Page {
    static url = "element/list"

    static at = {
        title ==~ /Element List/
    }

    static content = {
        newElement (to: ElementNewPage) {
            $ ('span.menuButton a.create')
        }

        elementTable {
            $ ("div.list table", 0)
        }

        elementRows {
            elementTable.find ('tbody').find ('tr')
        }

        row { row ->
            module (ElementRow, elementRows[row])
        }
    }

    def checkElementAtRow (String element, int rowNumber) {
        if (element == "Gold") {
            assert row (rowNumber).id.number
            assert row (rowNumber).atomicNumber == "79"
            assert row (rowNumber).name == 'Gold'
            assert row (rowNumber).symbol == 'Au'
        }
        else {
            assert false
        }
    }
}
