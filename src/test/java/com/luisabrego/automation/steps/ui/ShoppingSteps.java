package com.luisabrego.automation.steps.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.pages.CartPage;
import com.luisabrego.automation.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;

/** Product search and cart management through the UI. */
public class ShoppingSteps {

    private final TestContext context;

    public ShoppingSteps(TestContext context) {
        this.context = context;
    }

    @Given("I am on the products page")
    public void iAmOnTheProductsPage() {
        productsPage().open();
    }

    @When("I search for {string}")
    public void iSearchFor(String term) {
        productsPage().search(term);
    }

    @Then("I should see the searched products section")
    public void iShouldSeeTheSearchedProductsSection() {
        assertThat(productsPage().sectionTitle()).isEqualToIgnoringCase("Searched Products");
    }

    @Then("the search results should include {string}")
    public void theSearchResultsShouldInclude(String productName) {
        assertThat(productsPage().productNames()).contains(productName);
    }

    @When("I add the following products to the cart:")
    public void iAddTheFollowingProductsToTheCart(List<String> productNames) {
        ProductsPage page = productsPage();
        productNames.forEach(page::addToCart);
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        cartPage().open();
    }

    @Then("the cart should contain:")
    public void theCartShouldContain(List<Map<String, String>> expectedRows) {
        CartPage cart = cartPage();
        assertThat(cart.productNames())
                .containsExactlyInAnyOrderElementsOf(expectedRows.stream().map(row -> row.get("product")).toList());

        SoftAssertions softly = new SoftAssertions();
        for (Map<String, String> row : expectedRows) {
            String product = row.get("product");
            softly.assertThat(cart.priceOf(product)).as("price of %s", product).isEqualTo(row.get("price"));
            softly.assertThat(cart.quantityOf(product)).as("quantity of %s", product).isEqualTo(row.get("quantity"));
        }
        softly.assertAll();
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromTheCart(String productName) {
        cartPage().remove(productName);
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        assertThat(cartPage().isEmpty()).as("empty cart message visible").isTrue();
    }

    private ProductsPage productsPage() {
        return new ProductsPage(context.getDriver());
    }

    private CartPage cartPage() {
        return new CartPage(context.getDriver());
    }
}
