@ui @regression
Feature: Login
  As a registered customer
  I want to log in and out
  So that I can access my account safely

  Background:
    Given I am on the login page

  @smoke
  Scenario: Login with valid credentials
    Given a registered user exists
    When I log in with the registered user's credentials
    Then I should be logged in

  Scenario: Logout
    Given a registered user exists
    When I log in with the registered user's credentials
    And I log out
    Then I should be on the login page

  Scenario: Login with a wrong password
    Given a registered user exists
    When I log in with the registered user's email and password "WrongPass1!"
    Then I should see the login error "Your email or password is incorrect!"

  Scenario Outline: Login with an unregistered email
    When I log in with email "<email>" and password "<password>"
    Then I should see the login error "Your email or password is incorrect!"

    Examples:
      | email                           | password   |
      | not.registered.qa@example.com   | Secret123! |
      | another.unknown.qa@example.com  | Pass456!   |
