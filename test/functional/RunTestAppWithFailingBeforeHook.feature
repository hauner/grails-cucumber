Feature: test-app with failing before hook
    As a grails developer
    I want to see a scenario fail if the before hooks fails
    So that I can fix the before hook

@fail
Scenario: Hook Failure Scenario
   Given  I run test-app functional:cucumber
    When  the "before hook" scenario is executed
    Then  it should fail because of the before hook
