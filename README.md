# What's this? #
grails-cucumber is a [Grails](http://grails.org) plugin for [Cucumber](http://cukes.info)
based on [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm).

## Goals ##
* integrate cucumber (via cucumber-jvm) into the grails test runner as a grails test type
  "cucumber" to run cucumber scenarios via `grails test-app` and use [Geb](http://www.gebish.org/)
  to implement cucumber steps definitions

## Current State ##
It is not yet production ready... it is still a bit experimental but it is basically working. You
can run cucumber scenarios with the steps implemented in geb and you can use grails domain classes
in the steps as well. Take a look at the geb demo application in `tests/projects/geb`.

Cucumber-JVM is still a moving target, i.e. its api is still changing and it might brake the plugin
code when you are using the master branch code.

* cucumber is implemented as grails test type that gets added to the `functional` test phase, e.g.
to run the cucumber tests only use

```
grails test-app functional:cucumber
```

* you can run cucumber features with steps implemented using geb.
* cucumber features are expected in the `test/cucumber` folder using the typical cucumber
layout.

```
  test/cucumber/step_definitions/XxSteps.groovy
  test/cucumber/step_definitions/YySteps.groovy
  test/cucumber/step_definitions/...
  test/cucumber/support/env.groovy
  test/cucumber/support/...
  test/cucumber/Xx.feature
  test/cucumber/Yy.feature
```
* place geb modules and pages in the usual place as well:

```
  functional/modules/XRowModule.groovy
  functional/modules/...
  functional/pages/XxPage.groovy
  functional/pages/YyPage.groovy
```
* see the sample project in `tests/projects/geb` for a full example grails project
* the plugin integrates into grails usual test reporting. So failing steps, steps with errors or
undefined steps are all reported by grails as FAILED. The normal cucumber output can be found in
`target/test-reports`



## Todo ##

* keep up with api changes in cucumber-jvm
* the test report summary prints "Testcase: unknown took 0.065 sec", I do not yet understand why
  it prints "unknown" instead of the scenario name
* use grails like configuration for tags, language
* extend example

## Source Notes ##

* the plugin has a number of unit tests and a single "cli" test. There are also a number
of cucumber tests which are not meant to be run directly. They are only used by the "cli"
test. You can run the cli test with

```
grails test-app other:cli
```
