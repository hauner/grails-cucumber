package data

import books.Book


class Data {

    static def books = [
        [title: "Specification by Example", author: "Gojko Adzic"]
    ]

    static public def findByTitle (String title) {
        books.find { book ->
            book.title == title
        }        
    }

    static void createBookByTitle (String title) {
        new Book (findByTitle (title)).save ()
    }

    static void clearBooks () {
        Book.findAll()*.delete (flush: true)
    }
}
