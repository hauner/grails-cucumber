package books

class Book {
    String author
    String title

    static constraints = {
        title unique: true
    }
}
