package books

import grails.test.*

class BookTests extends GrailsUnitTestCase {
    def TITLE = "Specification by Example"
    def AUTHOR = "Gojko Adzic"

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShouldUseSymbolAsToString () {
        assertEquals ("$TITLE, $AUTHOR", new Book (title: TITLE, author: AUTHOR).toString ())
    }
}
