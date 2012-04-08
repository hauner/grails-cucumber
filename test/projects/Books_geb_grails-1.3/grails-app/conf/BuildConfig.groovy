grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.plugins.dir = 'plugins'
grails.plugin.location.cucumber = "../../.."

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }

    log "warn"
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal(null)
        //mavenCentral()
    }

    def gebVersion      = "0.6.3"
    def seleniumVersion = "2.20.0"

    dependencies {
        test ("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            exclude "xml-apis"
        }
        test ("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
        test ("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")

        test ("org.codehaus.geb:geb-junit4:$gebVersion")
    }

    plugins {
        test ":geb:$gebVersion"
        //test ":cucumber:0.2.3"
    }
}

