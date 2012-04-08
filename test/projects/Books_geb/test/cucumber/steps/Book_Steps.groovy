import pages.book.ListPage
import pages.book.NewPage
import pages.book.ShowPage
import data.Data

this.metaClass.mixin (cucumber.runtime.groovy.EN)


Given (~"^I open the book tracker\$") { ->
    to ListPage
    assert at (ListPage)
}

When (~"^I add \"([^\"]*)\"\$") { String bookTitle ->
    page.toNewPage ()
    assert at (NewPage)

    page.add (bookTitle)
}

Then (~"^I see \"([^\"]*)\"s details\$") { String bookTitle ->
    assert at (ShowPage)

    page.check2 (bookTitle)
}

Given(~"^I have already added \"([^\"]*)\"\$") { String bookTitle ->
    Data.createBookByTitle (bookTitle)
}

Then(~"^my book list contains \"([^\"]*)\"\$") { String bookTitle ->
    assert at (ListPage)

    page.checkBookAtRow (bookTitle, 0)
}
