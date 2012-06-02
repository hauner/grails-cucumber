import geb.binding.BindingUpdater
import geb.Browser
import data.Data

import static cucumber.runtime.groovy.Hooks.*


Before () {
    Data.clearBooks ()

    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()
}

After () {
    bindingUpdater.remove ()
}
