# Cucumber Grails Plugin #

grails-cucumber is a [Grails](http://grails.org) plugin for [Cucumber](http://cukes.info)
based on [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm). Cucumber-JVM is the JVM
implementation of cucumber with support for many JVM languages.

The plugin runs cucumber *inside* grails which allows us to call the grails api. For example it is
possible to call dynamic finders in the step implementations. You can populate the database with
test data or check domain objects written to the database.

See [Testing-Grails-with-Cucumber-and-Geb](https://github.com/hauner/grails-cucumber/wiki/Testing-Grails-with-Cucumber-and-Geb)
for a complete tutorial.

# Usage #

The plugin integrates cucumber as a [**grails test type**](http://ldaley.com/post/615966534/custom-grails-test)
into grails test infrastructure. That means you can run the cucumber features with `test-app` and
that the results will be included in the usual grails test reports.

Currently the plugin registers the cucumber test type only to the **functional test phase**. To run
the cucumber features you call grails by one of the following commands:

```
grails test-app functional:cucumber
grails test-app :cucumber
```

**The cucumber plugin does not provide an implementation for the functional test phase**. It depends
on another grails plugin, like [Geb](http://gebish.org), to provide it. Geb is currently the only
functional test plugin I have tried.

**The simplest possible functional test phase implementation is an empty `test/functional` directory. ;-)**
Grails will run the cucumber features if a `test/functional` directory exists. I'm not sure yet what
we can do in the steps here. Probably a lot since we have a running grails application in the functional
phase.


Cucumber-JVM is still a moving target, i.e. its api is still changing and it might brake the plugin
code when you are using the MASTER branch code.

* cucumber features are expected in the `test/cucumber` folder.
* the plugin integrates into grails usual test reporting. So failing steps, steps with errors or
undefined steps are all reported by grails as FAILED. The normal cucumber output can be found in
`target/test-reports`

# Changes #

see [CHANGES.md](https://github.com/hauner/grails-cucumber/blob/master/CHANGES.md)

# Development #

## Todo ##

* move `test/cucumber` to `test/functional/cucumber`
* run features without browser so we can test below the ui, surprise this works already... :-)
* support other grails functional test plugins
* keep up with api changes in cucumber-jvm
* the test report summary prints "Testcase: unknown took 0.065 sec", I do not yet understand why
  it prints "unknown" instead of the scenario name
* extend examples

## Tests ##

* the plugin has a number of unit tests and a single "cli" test. There are also a number of cucumber
tests which are not meant to be run directly. They are only used by the "cli" test. You can run the
cli test with

```
grails test-app other:cli
```

and the unit tests with

```
grails test-app unit:
```

The unit tests will also run from an IDE without starting up grails (works for me with IDEA 11.1).
