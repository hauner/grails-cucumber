Feature: mixing new elements
    As an alchemist
    I want to mix new elements
    just for fun :-)


Scenario: mixing "Gold"
   Given I am in my laboratory
    When I mix the secret ingredients of "Gold"
    Then I get "Gold" as a new element
     And my element list contains "Gold"
