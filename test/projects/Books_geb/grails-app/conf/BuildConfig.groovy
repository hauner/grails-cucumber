grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.work.dir = "target/work"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.plugins.dir = 'plugins'
grails.plugin.location.cucumber = "../../.."

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
        mavenLocal(null)

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    def gebVersion      = "0.9.2"
    def seleniumVersion = "2.41.0"

    dependencies {
//        compile "org.springframework:spring-orm:$springVersion"
        compile "org.springframework:spring-expression:$springVersion"
        compile "org.springframework:spring-aop:$springVersion"

        test ("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            exclude "xml-apis"
        }
        test ("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
        test ("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")

        test ("org.gebish:geb-junit4:$gebVersion")
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.53"

        // plugins for the compile step
        compile ":scaffolding:2.1.0"
        //compile ':cache:1.1.1'

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.5.3" // or ":hibernate:3.6.10.15"
        runtime ":database-migration:1.4.0"
        //runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.8"
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"

        // test
        test ":geb:$gebVersion"
    }
}
