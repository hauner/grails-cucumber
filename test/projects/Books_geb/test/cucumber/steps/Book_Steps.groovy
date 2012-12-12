import pages.book.ListPage
import pages.book.NewPage
import pages.book.ShowPage
import data.Data

import static cucumber.api.groovy.EN.*


Given (~'^I open the book tracker$') { ->
    to ListPage
    at ListPage
}

When (~'^I add "([^"]*)"$') { String bookTitle ->
    page.toNewPage ()
    at NewPage

    page.add (bookTitle)
}

Then (~'^I see "([^"]*)"s details$') { String bookTitle ->
    at ShowPage

    page.check2 (bookTitle)
}

Given (~'^I have already added "([^"]*)"$') { String bookTitle ->
    Data.createBookByTitle (bookTitle)
}

Then (~'^my book list contains "([^"]*)"$') { String bookTitle ->
    at ListPage

    page.checkBookAtRow (bookTitle, 0)
}
