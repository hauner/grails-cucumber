package pages.book

import geb.Page
import data.Data
import books.Book


class ShowPage extends Page {
    static at = {
        title ==~ /Show Book/
    }

    static content = {
        //list (to: ListPage) {
        //    $ ('a.list')
        //}

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
    }

    def check (String bookTitle) {
        def book = Data.findByTitle (bookTitle)

        assert id.number
        assert book.author == author
        assert book.title == btitle
    }

    def checkDB (String bookTitle) {
        Book book = Book.findByTitle (bookTitle)

        assert book.id == id.toLong ()
        assert book.author == author
        assert book.title == btitle
    }
}
