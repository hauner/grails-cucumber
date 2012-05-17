@i9n
Feature: list owned books
    As a book owner
    I want to list my books
    so that I can look up which books I own

#@ignore
Scenario: list existing books
   Given I have already added "Specification by Example"
    When I view the book list
    Then my book list contains "Specification by Example"
