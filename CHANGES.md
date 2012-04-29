### 0.4.1 ###

29th April 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.4.1))

* cucumber features & scenarios are correctly displayed in xml & html reports
* scenarios are properly reported by name and not as "TestCase: unknown"
* modified plugin formatter to report an `UndefinedStepException` so IntelliJs test listener does
  not crash (it does not implement all overloads of GrailsTestEventPublisher.testFailure).
* updated to cucumber-jvm 1.0.4


### 0.4.0 ###

16th April 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.4.0))

* configuration via `grails-app/conf/CucumberConfig.groovy`, see the plugin guide
* changed cucumber default path from `test/cucumber` to `test/functional`
* updated to cucumber-jvm 1.0.2
* cucumber-jvm is no longer embedded, it will be picked up by the usual grails dependency resolution
* started [plugin guide](https://github.com/hauner/grails-cucumber/wiki/Plugin-Guide)


### 0.3.0 ###

never released ([source code](https://github.com/hauner/grails-cucumber/tree/0.3.0))


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

