import geb.binding.BindingUpdater
import geb.Browser

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin (cucumber.runtime.groovy.EN)


def bindingUpdater

Before () {
    println "Before Hook"

    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()
}

After () {
    println "After Hook"

    bindingUpdater.remove ()
}
