package pages.book

import geb.Page
import modules.BookRow
import books.Book


class ListPage extends Page {
    static url = "book/list"

    static at = {
        title ==~ /Book List/
    }

    static content = {
        create (to: NewPage) {
            $ ('a.create')
        }

        bookTable {
            $ ("div.scaffold-list table", 0)
        }

        bookRows {
            bookTable.find ('tbody').find ('tr')
        }

        row { row ->
            module (BookRow, bookRows[row])
        }
    }

    def toNewPage () {
        create.click ()
    }

    def checkBookAtRow (String bookTitle, int rowNumber) {
        def book = Book.findByTitle (bookTitle)

        //assert book.id == row (rowNumber).id.toLong ()
        assert book.author == row (rowNumber).author2
        assert book.title == row (rowNumber).btitle2
    }
}
