package grails.plugin.cucumber

class Element {
    String symbol
    String name
    Integer atomicNumber

    static mapping = {
        table "element"
    }

    static constraints = {
    }

    String toString () {
        "$symbol"
    }
}
