Feature: test-app with missing feature steps
    As a grails developer
    I want to see code snippets for missing steps
    So that I can use the snippets to implement the steps

Scenario: No Steps
   Given  I run test-app cucumber:
    When  the "No Steps" scenario is executed
     And  an unimplemented step is found
    Then  it should print step snippets
