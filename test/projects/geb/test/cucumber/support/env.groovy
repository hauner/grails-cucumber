import geb.binding.BindingUpdater
import geb.Browser

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin (cucumber.runtime.groovy.EN)


def bindingUpdater

/*
World {
}
*/

Before () {
    ElementFactory.clearElements()

    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()
}

After () {
    bindingUpdater.remove ()
}
