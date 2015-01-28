### 1.1.0 ###

28th January 2015 ([source code](https://github.com/hauner/grails-cucumber/tree/1.1.0))

* updated to cucumber-jvm 1.2.2


### 1.0.1 ###

23rd August 2014 ([source code](https://github.com/hauner/grails-cucumber/tree/1.0.1))

* do not export unnecessary dependencies to avoid dependency issues in projects that use the plugin. 


### 1.0.0 ###

29th June 2014 ([source code](https://github.com/hauner/grails-cucumber/tree/1.0.0))

* updated to cucumber-jvm 1.1.7
* this release adds grails 2.3/2.4 support including forked-mode. To use forked-mode at least grails 2.3.8 is required, together with remote-control plugin at least grails 2.4.0.

> **Notes**

>To run cucumber features in forked-mode you must not call grails (gorm or services) in the steps
*directly* because application and features run in different JVMs. You can still run code in the
application by using the remote-control plugin. 

>If you are using geb you will probably have no grails (gorm or services) code in your steps but
the remote-control plugin might be still of interest for test data setup and tear down. 

>See [Cucumber with HTTPBuilder & RemoteControl][remote-control]. 


[remote-control]: http://softnoise.wordpress.com/2014/01/27/grails-cucumber-with-httpbuilder-remotecontrol/

### 0.10.0 ###

29th December 2013 ([source code](https://github.com/hauner/grails-cucumber/tree/0.10.0))

> **Note**

> grails-cucumber is not yet compatible with grails 2.3.x: **forked mode** does not work at all and **non forked mode** does not work with *in process* code that is directly calling domain classes, services or controllers in step code (like the 'Books' example). 

> **non forked mode** will be fixed in 0.11.0. **forked mode** requires a few fixes in grails. 

* updated to cucumber-jvm 1.1.5
* configure additional formatters. It is now possible to configure additional formatters in `CucumberConfig.groovy`:

        cucumber {
            // each line corresponds to a single cucumber `--format` option
            formats = [
                "json:target/test-reports/cucumber.json",
                "html:target/test-reports/cucumber"
            ]
        }

    The configuration of the additional formatters can be overridden on the command line (one time only):

        grails test-app :cucumber --format=json:target/test-reports/override.json

    The plugins (default) grails formatter will always run.


### 0.9.0 ###

18th July 2013 ([source code](https://github.com/hauner/grails-cucumber/tree/0.9.0))

* updated to cucumber-jvm 1.1.2.
* compile step files. It is now possible to compile the step files before running the features. There are two things to configure in `CucumberConfig.groovy`: first, the location of the source files using the  new `sources` configuration and second, the classpath of the steps using the `glue` configuration:

        cucumber {
            // steps, hooks etc that will be compiled
            sources = ["test/cucumber"]

            // .. and where cucumber will find the compiled steps & hooks
            glue = ["classpath:<the steps and hooks package>"]
        }

    A layout that separates the feature files from the step code may look like this:

        cucumber {
            // here we save the feature files...
            features = ["test/cucumber"]

            // steps, hooks etc that will be compiled
            // if the steps are in "test/functional" we do not need to configure it
            // sources = ["test/functional"]

            // .. and where cucumber will find the compiled steps & hooks
            glue = ["classpath:<the steps and hooks package>"]
        }


    #### Note ####
    The steps should be in their own package and there should be no other (normal) classes in this package.  Cucumber will load **all** classes in the given package(s). To avoid complications it is recommended to keep the steps in isolation. It is ok to put (normal) classes into the `sources` directories to compile them but they should be in a different package that is not listed in the `glue` configuration.

    See also [compiling steps in the guide][plugin guide compile] and the [compiled step article][compiled steps].

[plugin guide compile]: https://github.com/hauner/grails-cucumber/wiki/Plugin-Guide#compiledSteps
[compiled steps]: http://softnoise.wordpress.com/2013/05/20/grails-grails-cucumber-and-compiled-steps/


### 0.8.0 ###

9th January 2013 ([source code](https://github.com/hauner/grails-cucumber/tree/0.8.0))

* set dir/file:line filter on the command line. If the command line contains `:cucumber` and some
other arguments (i.e no `@tag`s) they are evaluated as file/dir and line filter for features and
scenarios. The filter info is just passed through to cucumber and can be whatever cucumber does
except. Here are a few examples:

        // features
        grails test-app :cucumber foo.feature bar.feature

        // feature with line numbers
        grails test-app :cucumber foo.feature:10:17

        // feature with "full" path
        grails test-app :cucumber test/functional/foo/bar.feature

        // feature directories
        grails test-app :cucumber foodir bardir

    See also [cli options][plugin guide cli].

[plugin guide cli]: https://github.com/hauner/grails-cucumber/wiki/Plugin-Guide#runningCli


### 0.7.0 ###

21st December 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.7.0))

* in order to catch pending steps in the test results they are reported as errors.
* updated to cucumber-jvm 1.1.1.

    ### Warning ###
    This release will break your build because of backwards-incompatible changes in cucumber-jvm. To
    fix your build adjust the cucumber `import` statements from:

        import static cucumber.runtime.groovy.EN.*
        import static cucumber.runtime.groovy.Hooks.*

    to:

        import static cucumber.api.groovy.EN.*
        import static cucumber.api.groovy.Hooks.*

    See [cucumber-jvm history](https://github.com/hauner/cucumber-jvm/blob/master/History.md) for more.


### 0.6.2 ###

3rd October 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.6.2))

* a scenario does fail now if a before hook failed (it was counted as passed).
* improved result counting (passed, failed, error),i.e. count scenarios only once.
* another reporting fix (end scenario notification could come twice).


### 0.6.1 ###

16th September 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.6.1))

* fixed another reporting bug (missing end scenario notification).
* updated to cucumber-jvm 1.0.14.


### 0.6.0 ###

23th June 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.6.0))

* removed automatic transaction rollback, see [this blog](transaction) for more. The short version
  is that it works only in limited situations (like integration test mode). We have to handle this
  ourself via `Before/After` hooks (or the `hooks` configuration).

* enable grails integration test mode with some simple configuration instead of hand rolling a
  `Before/After` hook pair. Put the following code into a cucumber groovy script (like env.groovy)
  to enable the grails [integration test](integration) features.

        hooks {
            // examples:
            transaction ()             // for all cucumber features/scenarios
            transaction ("@i9n")       // only for features/scenarios tagged with "@i9n"
        }
        
    The `transaction` parameters will be passed as tags to a pair of cucumber `Before` & `After` hooks.
    That means you can use the same tag syntax you would use for the cucumber hooks.

* set tags on the command line. If the command line contains `:cucumber`, `@tag` parameters are
  evaluated to filter execution of features or scenarios. Standard cucumber syntax applies without the
   `--tags` option keyword. Setting tags on the command line will overwrite `CucumberConfig.groovy`.

    Example:

        grails test-app :cucumber @foo,~@bar @zap

* improved reporting, the plugin no longer report a failing *step* as a failing *test*. It created
  quite confusing test reports.

[transactions]: http://softnoise.wordpress.com/2012/05/28/cucumber-grails-transaction-rollback
[integration]: http://grails.org/doc/latest/guide/testing.html#integrationTesting


### 0.5.0 ###

3rd May 2012 ([source code](https://github.com/hauner/grails-cucumber/tree/0.5.0))

* automatically rollback transaction, it is no longer necessary to create a before or after hook to
  clean up the database. It can be disabled by adding the cucumber tag `@notxn` to features or scenarios.


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

