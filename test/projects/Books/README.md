# a simple example grails project that is using cucumber with geb #

## Grails ##

This is a Grails 1.3.7 project.

## Running with Chrome ##
`grails -Dgeb.env=chrome -Dwebdriver.chrome.driver=<chromedriver> test-app functional:cucumber`

Replace `<chromedriver>` with the the full path to the chromedriver binary.


## Running with Firefox ##
`grails -Dgeb.env=firefox test-app functional:cucumber`
