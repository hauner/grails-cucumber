package books

import grails.converters.JSON

class BookController {
    def bookService

    def add () {
        render bookService.add (params) as JSON
    }

    def all () {
        def books = bookService.all ()
        render books as JSON
    }
}
