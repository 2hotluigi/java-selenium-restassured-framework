@api @regression
Feature: Products API
  As an API consumer
  I want to query the product catalogue
  So that I can show products and brands to customers

  @smoke
  Scenario: Get all products
    When I request the list of all products
    Then the response code should be 200
    And the products list should match the JSON schema
    And every product should have an id, name, price and brand

  Scenario: POST to the products list is not supported
    When I send a POST request to the products list
    Then the response code should be 405
    And the response message should be "This request method is not supported."

  Scenario: Get all brands
    When I request the list of all brands
    Then the response code should be 200
    And the brands list should include "Polo"

  Scenario Outline: Search products by term "<term>"
    When I search the API for "<term>"
    Then the response code should be 200
    And the API results should include the product "<product>"

    Examples:
      | term   | product          |
      | top    | Blue Top         |
      | tshirt | Men Tshirt       |
      | dress  | Sleeveless Dress |

  Scenario: Search without the search_product parameter
    When I search the API without the search parameter
    Then the response code should be 400
    And the response message should be "Bad request, search_product parameter is missing in POST request."
