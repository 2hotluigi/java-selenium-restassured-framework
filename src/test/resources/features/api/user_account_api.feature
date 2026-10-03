@api @regression
Feature: User account API
  As an API consumer
  I want to manage user accounts
  So that customers can register, log in and close their accounts

  @smoke
  Scenario: Full account lifecycle
    When I create a new account via the API
    Then the response code should be 201
    And the response message should be "User created!"
    When I verify the login with the account credentials
    Then the response code should be 200
    And the response message should be "User exists!"
    When I request the user details by email
    Then the response code should be 200
    And the user details should match the created account
    When I delete the account via the API
    Then the response code should be 200
    And the response message should be "Account deleted!"

  Scenario: Verify login with an unregistered user
    When I verify the login with email "nobody.qa.automation@example.com" and password "Wrong123!"
    Then the response code should be 404
    And the response message should be "User not found!"

  Scenario: Verify login without the email parameter
    When I verify the login without the email parameter
    Then the response code should be 400
    And the response message should be "Bad request, email or password parameter is missing in POST request."

  Scenario: DELETE on verify login is not supported
    When I send a DELETE request to verify login
    Then the response code should be 405
    And the response message should be "This request method is not supported."
