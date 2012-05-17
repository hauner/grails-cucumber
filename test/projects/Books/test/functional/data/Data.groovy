package data


class Data {
    static def books = [
        [title: "Specification by Example", author: "Gojko Adzic"]
    ]

    static public def findByTitle (String title) {
        books.find { book ->
            book.title == title
        }
    }
}
