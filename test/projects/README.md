# Examples #
this directory contains a couple of simple test/example projects.

> Note that all examples directly calling gorm, services or controllers etc from the steps does only
work with grails 2.3 in **non forked** mode. If you want to use forked mode you must **not** call
any grails api directly. You have to to use the remote-control plugin. See the Books_remote example.

## Books_remote ##
This is the newest example and is the preferred style for calling grails apis with grails 2.3 and
above.


### Running
	grails test-app
	grails test-app functional:cucumber


## Books_geb ##
Implements two features using the cucumber plugin and geb. The step implementations use geb to remote
control a web browser.

The example is described in [Testing-Grails-with-Cucumber-and-Geb](https://github.com/hauner/grails-cucumber/wiki/Testing-Grails-with-Cucumber-and-Geb)

### Running with Firefox
	grails -Dgeb.env=firefox test-app functional:cucumber

### Running with Chrome
	grails -Dgeb.env=chrome -Dwebdriver.chrome.driver=<chromedriver> test-app functional:cucumber

Replace `<chromedriver>` with the the full path to the chromedriver binary.


## Books ##
Implements two features using the cucumber plugin and _plain_ grails. The step implementations directly
call the controllers, no http or web browser involved.


### Running
	grails test-app
	grails test-app functional:cucumber


## Books_compile ##
Same as Books but configured to compile the steps.

### Running

see Books example.

