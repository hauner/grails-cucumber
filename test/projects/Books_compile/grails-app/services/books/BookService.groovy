package books


class BookService {
    Book add (Map params) {
        def newBook = new Book (params)
        newBook.save (failOnError: true)
        newBook
    }

    List<Book> all () {
        Book.findAll ()
    }
}
