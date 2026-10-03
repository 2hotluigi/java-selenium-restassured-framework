@ui @regression
Feature: Product search and cart
  As a shopper
  I want to find products and manage my cart
  So that I can buy what I need

  Background:
    Given I am on the products page

  Scenario Outline: Search products by term "<term>"
    When I search for "<term>"
    Then I should see the searched products section
    And the search results should include "<product>"

    Examples:
      | term   | product          |
      | top    | Blue Top         |
      | tshirt | Men Tshirt       |
      | dress  | Sleeveless Dress |

  @smoke
  Scenario: Add products to the cart
    When I add the following products to the cart:
      | Blue Top   |
      | Men Tshirt |
    And I open the cart
    Then the cart should contain:
      | product    | price   | quantity |
      | Blue Top   | Rs. 500 | 1        |
      | Men Tshirt | Rs. 400 | 1        |

  Scenario: Remove a product from the cart
    When I add the following products to the cart:
      | Blue Top |
    And I open the cart
    And I remove "Blue Top" from the cart
    Then the cart should be empty
