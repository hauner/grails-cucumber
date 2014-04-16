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

    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }

    log "warn" // Ivy resolver: 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        // cucumber
        compile ("info.cukes:cucumber-groovy:1.1.6") {
           excludes 'ant'   // avoid ant version conflict
        }
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.52.1"
        build ":release:3.0.1"
//        build (":release:2.2.1", ':rest-client-builder:1.0.3') {
//            export = false
//        }

        // plugins for the compile step
        compile ":scaffolding:2.0.3"
        //compile ':cache:1.1.1'

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.12" // or ":hibernate4:4.1.11.6"
        runtime ":database-migration:1.3.8"
        //runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.7"
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"
    }
}
