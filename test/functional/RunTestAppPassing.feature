Feature: test-app with a passing cucumber feature
    As a grails developer
    I want to see a passing cucumber feature
    so that I can verify the feature implementation 


Scenario: Passing Scenario
   Given  I run test-app functional:cucumber
    When  the "passing" scenario is executed
    Then  it should pass
