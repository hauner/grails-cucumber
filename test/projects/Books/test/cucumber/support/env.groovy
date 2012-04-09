import org.codehaus.groovy.grails.test.support.GrailsTestRequestEnvironmentInterceptor
import data.Data

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)


GrailsTestRequestEnvironmentInterceptor scenarioInterceptor

Before () {
    scenarioInterceptor = new GrailsTestRequestEnvironmentInterceptor (appCtx)
    scenarioInterceptor.init ()

    Data.clearBooks ()
}

After () {
    scenarioInterceptor.destroy ()
}
