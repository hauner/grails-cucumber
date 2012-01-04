package grails.plugin.cucumber

import grails.test.*


class ElementTests extends GrailsUnitTestCase {
    static String SYMBOL = 'H'

    protected void setUp () {
        super.setUp ()
    }

    protected void tearDown () {
        super.tearDown ()
    }

    void testShouldUseSymbolAsToString () {
        assertEquals (SYMBOL, new Element (symbol: SYMBOL).toString ())
    }
}
