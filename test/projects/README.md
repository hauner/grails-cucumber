# Examples #
this directory contains a couple of simple test/example projects.

## Books
Implements two features using the cucumber plugin and _plain_ grails. The step implementations directly call the controllers, no http or web browser involved.

### Running
	grails test-app
	grails test-app functional:cucumber


## Books_compile ##
Same as Books but configured to compile the steps.

### Running

see Books example.


## Books_geb ##
Implements two features using the cucumber plugin and geb. The step implementations use geb to remote control a web browser.

The example is described in [Testing-Grails-with-Cucumber-and-Geb](https://github.com/hauner/grails-cucumber/wiki/Testing-Grails-with-Cucumber-and-Geb)

### Running with Firefox
	grails -Dgeb.env=firefox test-app functional:cucumber

### Running with Chrome
	grails -Dgeb.env=chrome -Dwebdriver.chrome.driver=<chromedriver> test-app functional:cucumber

Replace `<chromedriver>` with the the full path to the chromedriver binary.
