grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.plugins.dir = 'plugins'
//grails.plugin.location."my-plugin" = "../my-plugin"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.release.scm.enabled = false
grails.project.repos.default = "grailsCentral"


grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }

    log "warn" // Ivy resolver: 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins ()
        grailsHome ()
        grailsCentral ()
        mavenLocal ()
        mavenCentral ()

        //grailsRepo "http://grails.org/plugins"
        //mavenRepo "https://oss.sonatype.org/content/repositories/snapshots"
    }

    plugins {
        build (":release:2.2.1", ':rest-client-builder:1.0.3') {
            export = false
        }

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.1" // or ":hibernate4:4.1.11.1"
    }

    dependencies {
        // cucumber
        compile ("info.cukes:cucumber-groovy:1.1.2") {
           excludes 'ant'   // avoid ant version conflict
        }
    }
}
