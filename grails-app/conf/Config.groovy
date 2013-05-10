// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
    appenders {
        console name: 'cucumberReporting', layout: pattern (conversionPattern: '%c{1} - %m')
        console name: 'FeatureReport',     layout: pattern (conversionPattern: 'FeRe - %m')
        console name: 'CucumberFormatter', layout: pattern (conversionPattern: 'CuFo - %m')
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'

//    trace cucumberReporting: 'grails.plugin.cucumber', additivity: false
//    trace FeatureReport: 'grails.plugin.cucumber.FeatureReport', additivity: false
//    trace CucumberFormatter: 'grails.plugin.cucumber.CucumberFormatter', additivity: false
}

grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
