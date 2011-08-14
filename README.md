# What's this? #

grails-cucumber is a [Grails](http://grails.org) plugin for
[Cucumber](http://cukes.info).


## Goals ##

* integrate cucumber (via cuke4duke) into the grails test runner as a
  grails test type "cucumber" and run the cucumber scenarios with
  `grails test-app`
* use [Geb](http://www.gebish.org/) in cucumber (groovy) steps definitions


## Current State ##

It is not yet finished... but it is basically working:

* it does run the plugin test feature "dummy.feature" with groovy step
  definitions.
* the cucumber features are stored in the `test/cucumber` folder (along the
  other grails test stuff) and the (groovy) step definitions in
  `test/cucumber/step_definitions`
* run only cucumber features with `grails test-app cucumber:`


### Todo ###

* create grails like test-report
* remove progress/debug println stuff
* run cucumber scenarios against the grails app (enable `functionalTestPhase`)
* examples
* setup cuke4duke when installing the plugin (did not work!?)
* get scenario count by running cucumber with --dry-run (cuke4duke crashes!?)
* use grails like configuration
* remove hardcoded jruby/cuke4duke versions
* make geb work, extend cucumber classpath


## Approach ##

The plugin adds a a new grails custom test type called "cucumber" based on
the article [Grails custom test type](
http://ldaley.com/post/615966534/custom-grails-test).

Cucumber gets started via cuke4duke. The plugin does not use the cuke4duke
ant tasks. It does run cuke4duke directly using JRubys embed api (Red Bridge).

JRuby will be provided by grails dependencies mechanism and the plugin will
automatically install the cuke4duke gem at first run.
