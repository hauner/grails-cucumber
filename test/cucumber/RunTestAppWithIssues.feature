Feature: test-app with broken features
    As a grails developer
    I want to see failing or braking cucumber steps
    So that I can fix them

Scenario: Failure Scenario
   Given  I run test-app cucumber:
    When  the "Failure" scenario is executed
    Then  it should fail
     And  report the failing step

Scenario: Error Scenario
   Given  I run test-app cucumber:
    When  the "Failure" scenario is executed
    Then  it should error
     And  report the erroneous step
