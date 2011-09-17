# What's this? #

grails-cucumber is a [Grails](http://grails.org) plugin for [Cucumber]
(http://cukes.info) based on cucumber-jvm [Cucumber-JVM]
(https://github.com/cucumber/cucumber-jvm).


## Goals ##

* integrate cucumber (via cucumber-jvm) into the grails test runner as a
  grails test type "cucumber" to run cucumber scenarios via `grails test-app`
* use [Geb](http://www.gebish.org/) in cucumber (groovy) steps definitions


## Current State ##

It is not yet production ready... but it is basically working:

* it does run a super simple test feature "dummy.feature" with groovy step
  definitions.
* the cucumber features are stored in the `test/cucumber` folder (along the
  other grails test stuff) and the (groovy) step definitions in
  `test/cucumber/step_definitions`
* run only cucumber features with `grails test-app cucumber:`


### Todo ###

* create grails like test-report (done)
* the test report summary prints "Testcase: unknown took 0.065 sec", I do not
  yet understand why it prints unknown instead of the scenario name
* remove progress/debug println stuff
* run cucumber scenarios against the grails app (enable `functionalTestPhase`)
* use grails like configuration for tags, language
* examples
