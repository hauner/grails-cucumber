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
        //mavenRepo "https://oss.sonatype.org/content/repositories/snapshots"
        //mavenCentral()
    }

    def cucumberVersion = "1.0.0.RC21-SNAPSHOT"
    
    plugins {
        test (':spock:0.5-groovy-1.7') {
            export = false
        }
    }

    dependencies {
        // scopes: 'build', 'compile', 'runtime', 'test' or 'provided'

        // to have a stable version of cucumber-jvm we provide the jars inside
        // the lib folder and we do not download them via grails dependencies

        // cucumber
        //compile ("info.cukes:cucumber-groovy:${cucumberVersion}") {
        //   excludes 'ant'   // avoid ant version conflict
        //}

        // spock
        test ('org.objenesis:objenesis:1.2') {
            export = false
        }
    }
}
