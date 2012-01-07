import grails.plugin.cucumber.Element

class ElementFactory {
    static void createElement (String element) {
        if (element == 'Gold') {
            new Element (atomicNumber: 79, name: 'Gold', symbol: 'Au').save ()
        }
        else {
            assert false
        }
    }

    static void clearElements () {
        Element.findAll()*.delete (flush: true)
    }
}
