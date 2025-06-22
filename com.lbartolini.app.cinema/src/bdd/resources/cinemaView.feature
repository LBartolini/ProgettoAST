Feature: Cinema Application
  Feature specification for Cinema Application
  
  Background:
    Given The Database contains some films and users
    And The Cinema View is shown

  Scenario: User logs in
    Given The user provides a username in the text field
    When The user click the "Login" button
    Then The ticket list shows all the tickets purchased by that user
    
  Scenario: User logs in with a not registered username
    Given The user provides a not registered username in the text field
    When The user click the "Login" button
    Then An error is shown
    
  Scenario: User buys base ticket
    Given The user provides a username in the text field
    Given The user selects a film from the film list
    When The user clicks the "Buy Base" button
    Then The ticket list shows all the tickets purchased by that user, in particular the one just purchased
    
  Scenario: User buys premium ticket
    Given The user provides a username in the text field
    Given The user selects a film from the film list
    When The user clicks the "Buy Premium" button
    Then The ticket list shows all the tickets purchased by that user, in particular the one just purchased
