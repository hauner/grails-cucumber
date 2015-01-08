grails.project.work.dir = "target/work"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.plugins.dir = 'plugins'
//grails.plugin.location."my-plugin" = "../my-plugin"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.release.scm.enabled = false
grails.project.repos.default = "grailsCentral"


/*
grails.project.fork = [
]
*/

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global")

    log "warn"

    repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        // cucumber
        compile ("info.cukes:cucumber-groovy:1.2.0") {
           excludes 'ant' // avoid ant version conflict
        }

        // spock support
        test ("cglib:cglib-nodep:3.1") {
            export = false
        }
    }

    plugins {
        build (":tomcat:7.0.53", ":release:3.0.1") {
            export = false
        }
    }
}
