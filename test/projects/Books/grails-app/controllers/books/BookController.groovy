package books

import grails.converters.JSON

class BookController {
    def bookService

    def add () {
        //def book = bookService.add (params)
        //render book as JSON

        render bookService.add (params) as JSON
    }

    def all () {
        def books = bookService.all ()
        render books as JSON
    }
}
