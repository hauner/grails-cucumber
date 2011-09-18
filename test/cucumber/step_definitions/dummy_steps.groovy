import static groovy.util.GroovyTestCase.assertEquals

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin (cucumber.runtime.groovy.EN)


World {
    println "WORLD!"
}

Given(~"^Dummy_Given\$") { ->
    println "Step Given"
    //assertEquals ("success", "failure")
}

Then(~"^Dummy_Then\$") { ->
    println "Step When"
    //int i = 20 / 0
}

When(~"^Dummy_When\$") { ->
    println "Step Then"
}

