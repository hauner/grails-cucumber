# Cucumber Grails Plugin User Guide

## Introduction

grails-cucumber is a [Grails][] plugin for [Cucumber][] based on [Cucumber-JVM][]. Cucumber-JVM is the JVM implementation of cucumber and it supports many JVM languages. Groovy is one of them.

grails-cucumber is a *functional* test plugin for grails. It is not meant to implement tests on the unit test level. It is only available in the *functional* test phase.

The plugin runs cucumber *inside* a running grails application which allows us to use the grails api in the cucumber step implementations. We can call GORM stuff like dynamic finders, services or even controllers.

[grails]: http://grails.org
[cucumber]: http://cukes.info
[cucumber-jvm]: https://github.com/cucumber/cucumber-jvm

## Plugin Installation

grails-cucumber is a normal grails plugin. To use it in your grails application just add it to the `plugins` configuration block in `BuildConfig.groovy` like this (using the latest availabe version of the plugin):

    plugins {
       test ":cucumber:0.4.0"
    }
 
## `.feature`  Files Location
The only convention regarding cucumber `.feature` files is that they should be in the `test/functional` directory. Inside it you can use *any* layout you like to structure the features.

> The plugin will tell Cucumber-JVM to look for features in this directory and Cucumber-JVM will scan it recursivly.

If you need to you can override this path. See Plugin Configuration.

## Step Implementations Location
The step implementations follow the same convention as the `.feature`s. They should be in `test/functional` and inside it you can use *any* layout you like to structure the steps.

>The plugin will tell Cucumber-JVM to look for the steps in this directory and Cucumber-JVM will scan it recursivly.
 
>Note that you should not mix *cucumber groovy scripts* with other groovy scripts. Cucumber-JVM will run *all* groovy scripts it will find in the given path. Chances are high that this will fail and is probably not what you want anyway.

If you need to you can override this path. See Plugin Configuration.

## Step Implementations
This section does contain some basic information for implementing steps: What is possible at all and how do I use specific grails functionality.

### using GORM

### using Services

### using Controllers
#### @Before & @After Code

### using Geb            
See [Testing-Grails-with-Cucumber-and-Geb][cucumber-geb] for a complete tutorial.

#### @Before & @After Code
 
 [cucumber-geb]: https://github.com/hauner/grails-cucumber/wiki/Testing-Grails-with-Cucumber-and-Geb
 
## Running Cucumber Features

The plugin integrates cucumber as a [**grails test type**][testtype] into the grails test infrastructure. You can run the cucumber features via the `grails test-app` command and the test results will be included in the normal grails test reports.

Currently the plugin registers the cucumber test type only to the **functional test phase**. To run the cucumber features you call grails by one of the following commands:

    grails test-app functional:cucumber
    grails test-app :cucumber

Or, if you do not use a second *functional* test plugin you can also use:

    grails test-app functional:

[testtype]: http://ldaley.com/post/615966534/custom-grails-test

### Test Reports
The plugin integrates into grails test reporting. So *failing* steps, steps with *errors* or *undefined* steps are all reported by grails as FAILED. The normal cucumber output can be found in `target/test-reports`.
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
## Plugin Configuration

### Convention over Configuration
The plugin does use a couple of default settings that can be overriden by `CucumberConfig.groovy` if you need to. If a setting is not given in the configuration file the default will be used.

The defaults in `CucumberConfig.groovy` notation are:

    cucumber {
        tags = []
        
        features = ["test/functional"]
        steps = ["test/functional"]
    }

The hardcoded `test/cucumber` path used in the plugin before version 0.4.0 is now deprecated. If you still like to use it you can add it to the `features` and `steps` setting in  `CucumberConfig.groovy`.

    cucumber {
        features = ["test/cucumber"]
        steps = ["test/cucumber"]
    }
    
### Location
The plugin will read the configuration from `grails-app/conf/CucumberConfig.groovy`. It is a a standard config slurper file.

> We can adjust the paths where cucumber will look for  `.feature` files and step implementations so putting the configuration along the cucumer files does not work.

### Options

#### Tags
Cucumber tags can be set using the `tags` configuration option:

    cucumber {
        tags = ["@implemented", "~@ignored"]
    }

`tags` is a list of strings where each item corresponds to a single cucumber `--tags` option.  See the cucumber documentation to read more about the [`--tags`][tags] switch. 

#### `.feature` File Location
To override or extend the locations where cucumber will search for `.feature` files you can use the `features` option:

    cucumber {
        features = [
            "test/functional/features",
            "test/functional/featuresOfCurrentSprint"
        ]
    }

`features` is a list of paths where each path is relative (below) the grails application root directory.

> Note that settings this option will *override* the default `test/functional` path. If you want to keep it you have to add it to the `features` path list.

[tags]: https://github.com/cucumber/cucumber/wiki/Tags

#### Step Implementation Location
To override or extend the locations cucumber will search for groovy scripts with gherkin steps or hooks you can use the `glue` option:

    cucumber {
        glue = [
            "test/functional/steps",
            "test/functional/hooks"
        ]
    }

`glue` is a list of paths where each path is relative (below) the grails application root directory.

> Note that settings this option will *override* the default `test/functional` path. If you want to keep it you have to add it to the `glue` path list.

## FAQ & Pitfalls

* **The plugin fails when I try to run features in a non-english language.**

    If cucumber does not recognize the `# language: <language code> ` comment at the beginning of a feature file it is probably because of a byte order marker ([BOM][]). Remove it and try again.

[bom]: http://en.wikipedia.org/wiki/Byte_order_mark