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
        mavenLocal (null)
        grailsRepo "http://grails.org/plugins"
        //mavenRepo "https://oss.sonatype.org/content/repositories/snapshots"
        //mavenCentral()
    }

    def cucumberVersion = "1.0.4"
    
    plugins {
        build (":release:2.0.0") {
            export = false
        }

        test (':spock:0.6') {
            export = false
        }
    }

    dependencies {
        // cucumber
        compile ("info.cukes:cucumber-groovy:${cucumberVersion}") {
           excludes 'ant'   // avoid ant version conflict
        }

        // spock
        test ('org.objenesis:objenesis:1.2') {
            export = false
        }
    }
}
