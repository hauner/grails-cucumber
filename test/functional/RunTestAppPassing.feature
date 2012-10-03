Feature: test-app with passing cucumber features
    As a grails developer
    I want to see passing cucumber features
    so that I can verify their implementation


Scenario: First Passing Scenario
   Given  I run test-app functional:cucumber
    When  the "first passing" scenario is executed
    Then  it should pass

Scenario: Second Passing Scenario
   Given  I run test-app functional:cucumber
    When  the "second passing" scenario is executed
    Then  it should pass
