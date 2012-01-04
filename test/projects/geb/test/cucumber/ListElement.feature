Feature: list existing elements
    As an alchemist
    I want to list my existing elements


Scenario: list existing Elements
   Given "Gold" already exists
     And I am in my laboratory
    Then my element list contains "Gold"
