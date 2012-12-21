### 0.7.0 ###

21st December 2012 ([source code](https://github.com/hauner/tree/0.7.0))

* in order to catch pending steps in the test results they are reported as errors.
* updated to cucumber-jvm 1.1.1.
    > ### Warning ###
> This release will break your build because of backwards-incompatible changes in cucumber-jvm. To
> fix your build adjust the cucumber `import` statements from:
> 
>       import static cucumber.runtime.groovy.EN.*
>       import static cucumber.runtime.groovy.Hooks.*
>
> to:
>
>       import static cucumber.api.groovy.EN.*
>       import static cucumber.api.groovy.Hooks.*
>
> See [cucumber-jvm history](https://github.com/hauner/cucumber-jvm/blob/master/History.md) for more.


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
   `--tags` option keyword. Setting tags on the comand line will overwrite `CucumberConfig.groovy`.

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

