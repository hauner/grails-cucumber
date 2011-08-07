grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.plugins.dir = 'plugins'
//grails.plugin.location."my-plugin" = "../my-plugin"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

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
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    plugins {
        test ':spock:0.5-groovy-1.7'
    }

    cuke4duke = "0.4.4"
    jruby     = "1.6.3"

    dependencies {
        // scopes: 'build', 'compile', 'runtime', 'test' or 'provided'

        test ("org.jruby:jruby-complete:${jruby}")
        test ("cuke4duke:cuke4duke:${cuke4duke}") {
            transitive = false  // avoids ant errors
        }
    }
}
