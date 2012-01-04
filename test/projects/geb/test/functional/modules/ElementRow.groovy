package modules

import geb.Module


class ElementRow extends Module {
    static content = {
        cell { column ->
            $ ('td', column)
        }
        
        cellText { column ->
            cell (column).text ()
        }

        id {
            cellText (0)
        }

        atomicNumber {
            cellText (1)
        }

        name {
            cellText (2)
        }

        symbol {
            cellText (3)
        }
    }
}
