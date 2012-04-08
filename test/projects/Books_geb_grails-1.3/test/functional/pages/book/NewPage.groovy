package pages.book

import geb.Page
import data.Data

class NewPage extends Page {
    static at = {
        title ==~ /Create Book/
    }

    static content = {
        save {
            $ ('input.save')
        }
    }

    def add (String bookTitle) {
        def book = Data.findByTitle (bookTitle)
        
        if (book.title == bookTitle) {
            $ ("form").author = book.author
        }
        $ ("form").title = bookTitle

        save.click ()
    }
}
