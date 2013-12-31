package world

import data.Data
import grails.plugin.remotecontrol.RemoteControl
import static javax.servlet.http.HttpServletResponse.*


class Books {
    def booksRequestData

    def getBooksResponse () {
        booksRequestData.response
    }

    def getBooksResponseData () {
        booksRequestData.data
    }

    Long setupBook (String title) {
        def remote = new RemoteControl ()

        def book = Data.findByTitle (title)
        Long id = remote {
            ctx.bookService.add (book)?.id
        } as Long

        assert id
        id
    }

    void requestAddBook (String title) {
        def newBook = Data.findByTitle (title)
        booksRequestData = post ('book/add', newBook)
        assert booksResponse.status == SC_OK
    }

    void assertAddBook (String title) {
        def expected = Data.findByTitle (title)
        def actual = booksResponseData

        assert actual.id
        assert actual.title  == expected.title
        assert actual.author == expected.author
    }

    void requestAllBooks () {
        booksRequestData = getJson ('book/all')
        assert booksResponse.status == SC_OK
    }

    void assertAllBooksContains (Long id, String title) {
        def expected = Data.findByTitle (title)
        def actual = booksResponseData.first ()

        assert actual.id     == id
        assert actual.title  == expected.title
        assert actual.author == expected.author
    }
}
