package pages.book

import geb.Page
import data.Data


class ShowPage extends Page {
    static at = {
        title ==~ /Show Book/
    }

    static content = {
        // grails 1.3
        row { val ->
            $ ('td.name', text: val).parent ()
        }

        value { val ->
            row (val).find ('td.value').text ()
        }

        id {
            value ('Id')
        }

        btitle {
            value ('Title')
        }

        author {
            value ('Author')
        }

        // grails 2.0
        row2 { val ->
            $ ('span.property-label', text: val).parent ()
        }

        value2 { val ->
            row2 (val).find ('span.property-value').text ()
        }

        author2 {
            value2 ('Author')
        }

        btitle2 {
            value2 ('Title')
        }
    }

    def check (String bookTitle) {
        def book = Data.findByTitle (bookTitle)

        assert id.number
        assert book.author == author
        assert book.title == btitle
    }

    def check2 (String bookTitle) {
        def book = Data.findByTitle (bookTitle)

        assert book.author == author2
        assert book.title == btitle2
    }
}
