package steps


import static cucumber.api.groovy.EN.*


Given (~'^I open the book tracker$') { ->
    // nop
}

When (~'^I add "([^"]*)"$') { String bookTitle ->
    requestAddBook (bookTitle)
}

Then (~'^I see "([^"]*)"s details$') { String bookTitle ->
    assertAddBook (bookTitle)
}

Given (~'^I have already added "([^"]*)"$') { String bookTitle ->
    bookId = setupBook (bookTitle)
}

When (~'^I view the book list$') { ->
    requestAllBooks ()
}

Then (~'^my book list contains "([^"]*)"$') { String bookTitle ->
    assertAllBooksContains (bookId, bookTitle)
}
