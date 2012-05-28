import static grails.plugin.cucumber.Hooks.hooks

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)


hooks {
    integration ("@i9n")
}
