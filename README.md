# What's this? #

grails-cucumber-jvm is a [Grails](http://grails.org) plugin for
[Cucumber](http://cukes.info).


## Goals ##

* integrate cucumber (via cucumber-jvm) into the grails test runner as a
  grails test type "cucumber" to run cucumber scenarios via `grails test-app`
* use [Geb](http://www.gebish.org/) in cucumber (groovy) steps definitions


## Current State ##

It is not yet finished... but it is basically working:

* it does run a super simple test feature "dummy.feature" with groovy step
  definitions.
* the cucumber features are stored in the `test/cucumber` folder (along the
  other grails test stuff) and the (groovy) step definitions in
  `test/cucumber/step_definitions`
* run only cucumber features with `grails test-app cucumber:`


### Todo ###

* create grails like test-report (done)
* the test report summary prints "Testcase: unknown took 0.065 sec", I do not
  yet understand why it prints unknown instead of the sceario name
* remove progress/debug println stuff
* run cucumber scenarios against the grails app (enable `functionalTestPhase`)
* use grails like configuration for tags, language
* examples


## Approach ##

The plugin adds a a new grails custom test type called "cucumber" based on
the article [Grails custom test type](
http://ldaley.com/post/615966534/custom-grails-test).


Cucumber gets started via cuke4duke. The plugin does not use the cuke4duke
ant tasks. It does run cuke4duke directly using JRubys embed api (Red Bridge).

JRuby will be provided by grails dependencies mechanism and the plugin will
automatically install the cuke4duke gem at first run.
