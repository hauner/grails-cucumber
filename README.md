# Cucumber Grails Plugin #

grails-cucumber is a [Grails][] plugin for [Cucumber][] based on [Cucumber-JVM][]. Cucumber-JVM
is the JVM implementation of cucumber with support for many JVM languages.

The plugin runs cucumber *inside* grails which allows us to call the grails api. For example it is possible
to call dynamic finders in the step implementations. You can populate the database with test data or check
domain objects written to the database.

See the [wiki][github wiki]  for more information.

[grails]: http://grails.org
[cucumber]: http://cukes.info
[cucumber-jvm]: https://github.com/cucumber/cucumber-jvm
[github wiki]: https://github.com/hauner/grails-cucumber/wiki
[plugin guide]: https://github.com/hauner/grails-cucumber/wiki/Plugin-Guide
[article geb]: https://github.com/hauner/grails-cucumber/wiki/Testing-Grails-with-Cucumber-and-Geb
[article]: https://github.com/hauner/grails-cucumber/wiki/Automating-Specification-with-Cucumber-and-Grails

# Quick Usage #

The plugin integrates cucumber as a [**grails test type**][grails testtype] into the grails test infrastructure.
That means you can run the cucumber features with `test-app` and that the results will be included in the usual
grails test reports.

Currently the plugin registers the cucumber test type only to the **functional test phase**. To run
the cucumber features you call grails by one of the following commands:

	grails test-app functional:cucumber
	grails test-app :cucumber

* cucumber features and steps are expected in the `test/functional` folder.
* the plugin integrates into grails usual test reporting. So failing steps, steps with errors or
undefined steps are all reported by grails as FAILED. The normal cucumber output can be found in
`target/test-reports`

[grails testtype]: http://ldaley.com/post/615966534/custom-grails-test

# Changes #

see [CHANGES.md](https://github.com/hauner/grails-cucumber/blob/master/CHANGES.md)

# Development #

## Todo ##

* support other grails functional test plugins
* the test report summary prints "Testcase: unknown took 0.065 sec", I do not yet understand why
  it prints "unknown" instead of the scenario name
* extend examples

## Tests ##

* the plugin has a number of unit tests and a single "cli" test. There are also a number of cucumber
tests which are not meant to be run directly. They are only used by the "cli" test. You can run the
cli test with

`grails test-app other:cli`

and the unit tests with

`grails test-app unit:`

The unit tests will also run from an IDE without starting up grails (works for me with IDEA 11.1).
