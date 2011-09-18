Feature: test-app with succeeding features
    As a grails developer
    I want to see succeeding features

Scenario: Successful Scenario
   Given  I run test-app cucumber:
    When  the "Success" scenario is executed
    Then  it should pass
