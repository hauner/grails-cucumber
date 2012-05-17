@i9n
Feature: new book entry
    As a book owner
    I want to add books I own to the book tracker
    so that I do not have to remember them by myself

Scenario: new book
   Given I open the book tracker
    When I add "Specification by Example"
    Then I see "Specification by Example"s details
