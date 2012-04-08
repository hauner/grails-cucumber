import geb.binding.BindingUpdater
import geb.Browser
import data.Data

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)

def bindingUpdater

Before () {
    Data.clearBooks ()

    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()
}

After () {
    bindingUpdater.remove ()
}
