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

// uncomment (and adjust settings) to fork the JVM to isolate classpaths

grails.project.fork = [
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256],
    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
         // runtime 'mysql:mysql-connector-java:5.1.29'
         // runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'

//        compile "org.springframework:spring-orm:$springVersion"
//        compile "org.springframework:spring-expression:$springVersion"
//        compile "org.springframework:spring-aop:$springVersion"

        compile ("org.codehaus.groovy.modules.http-builder:http-builder:0.6") {
            excludes 'groovy', 'xml-apis'
        }

        // spock support
        test "cglib:cglib-nodep:3.1"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.55"

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        //compile ':cache:1.1.7'
        //compile ":asset-pipeline:1.9.6"
        compile ":remote-control:1.4"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.5.5" // or ":hibernate:3.6.10.17"
        //runtime ":database-migration:1.4.0"
        //runtime ":jquery:1.11.1"
        //compile ":sass-asset-pipeline:1.9.0"
        //compile ":less-asset-pipeline:1.10.0"
        //compile ":coffee-asset-pipeline:1.8.0"
        //compile ":handlebars-asset-pipeline:1.3.0.3"
    }
}
