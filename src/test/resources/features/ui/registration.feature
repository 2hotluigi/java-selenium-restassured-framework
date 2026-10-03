@ui @regression
Feature: User registration
  As a new visitor
  I want to create an account
  So that I can place orders

  @smoke
  Scenario: Register a new user and delete the account
    Given I am on the home page
    When I start the signup with a new user
    And I fill in the account information
    And I submit the account creation form
    Then I should see the "Account Created!" message
    When I continue to the home page
    Then I should be logged in
    When I delete my account
    Then I should see the "Account Deleted!" message

  Scenario: Signup with an email that is already registered
    Given a registered user exists
    And I am on the login page
    When I start the signup with the registered user's email
    Then I should see the signup error "Email Address already exist!"
