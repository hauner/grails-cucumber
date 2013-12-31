package hooks

import static cucumber.api.groovy.Hooks.Before


Before () {
    resetDatabase ()
}
