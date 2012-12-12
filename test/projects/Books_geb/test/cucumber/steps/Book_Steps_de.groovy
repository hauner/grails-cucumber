import static cucumber.api.groovy.DE.*

import pages.book.ListPage
import data.Data


Wenn (~"^ich die Bücherliste aufrufe\$") { ->
    to ListPage
    assert at (ListPage)
}

Wenn (~"^ich \"([^\"]*)\" bereits hinzugefügt habe\$") { String bookTitle ->
    Data.createBookByTitle (bookTitle)
}

Dann (~"^beinhaltet die Bücherliste das Buch \"([^\"]*)\"\$") { String bookTitle ->
    assert at (ListPage)

    page.checkBookAtRow (bookTitle, 0)
}
