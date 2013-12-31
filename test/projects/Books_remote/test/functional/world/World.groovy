package world

import grails.plugin.remotecontrol.RemoteControl

import static cucumber.api.groovy.Hooks.World



class BookWorld {
    def binding

    BookWorld (def binding) {
        this.binding = binding
    }

    void resetDatabase () {
        def remote = new RemoteControl ()

        boolean success = remote {
            ctx.databaseService.reset ()
            true
        }
        assert success
    }

//    def getFixtureLoader () {
//        getBean ("fixtureLoader")
//    }
//
//    def getBean (String bean) {
//        binding.appCtx.getBean (bean)
//    }
}




World () {
//    change all worlds
//    OurWorld.mixin Greeter, Fareweller
//    def world = new MyWorld ()

//    change specific world
//    def world = new Object()
//    world.metaClass.mixin Greeter, Fareweller

    def world = new BookWorld (binding)
    world.metaClass.mixin Requests
    world.metaClass.mixin Books
    world
}
