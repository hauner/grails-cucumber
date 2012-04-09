### 0.3.0 - unreleased ###

x-th April 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.3.0))

* read configuration file `test/cucumber/CucumberConfg.groovy`

    available configuration:

    `tags = ["@tag1", "@tag2"]`, were each value gets passed to cucumber as a single `--tags`
    option (see cucumber help for tag variations)

* updated to cucumber-jvm 1.0.2
* cucumber-jvm is no longer embedded, it will be picked up by the usual grails dependency resolution


### 0.2.3 ###

4th March 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.2.3))

* fixed formatter crash when a before hook throws
* updated to cucumber-jvm api RC21
* updated embedded cucumber-jvm to RC21


### 0.2.2 ###

10th January 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.2.2))

  * fixed grails 2.0.0 incompatibility


### 0.2.1 ###

8th January 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.2.1))

* do not export the plugins test dependencies


### 0.2.0 ###

7th January 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.2.0))

* first usable version
* use geb to implement steps
* call domain class foo from steps


