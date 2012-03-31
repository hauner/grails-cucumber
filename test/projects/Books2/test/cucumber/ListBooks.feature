Feature: list owned books
    As a book owner
    I want to list my books
    so that I can look up which books I own

Scenario: list existing books
   Given I have already added "Specification by Example"
     And I open the book tracker
    Then my book list contains "Specification by Example"
