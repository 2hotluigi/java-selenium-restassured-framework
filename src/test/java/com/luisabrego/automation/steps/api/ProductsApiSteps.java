package com.luisabrego.automation.steps.api;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import com.luisabrego.automation.api.ApiResponses;
import com.luisabrego.automation.api.ProductsApi;
import com.luisabrego.automation.api.models.BrandsResponse;
import com.luisabrego.automation.api.models.ProductsResponse;
import com.luisabrego.automation.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.MatcherAssert;

public class ProductsApiSteps {

    private static final String PRODUCTS_SCHEMA = "schemas/products-list.schema.json";

    private final TestContext context;

    public ProductsApiSteps(TestContext context) {
        this.context = context;
    }

    @When("I request the list of all products")
    public void iRequestTheListOfAllProducts() {
        context.setResponse(ProductsApi.getAllProducts());
    }

    @When("I send a POST request to the products list")
    public void iSendAPostRequestToTheProductsList() {
        context.setResponse(ProductsApi.postToProductsList());
    }

    @When("I request the list of all brands")
    public void iRequestTheListOfAllBrands() {
        context.setResponse(ProductsApi.getAllBrands());
    }

    @When("I search the API for {string}")
    public void iSearchTheApiFor(String term) {
        context.setResponse(ProductsApi.searchProduct(term));
    }

    @When("I search the API without the search parameter")
    public void iSearchTheApiWithoutTheSearchParameter() {
        context.setResponse(ProductsApi.searchProductWithoutParameter());
    }

    @Then("the products list should match the JSON schema")
    public void theProductsListShouldMatchTheJsonSchema() {
        MatcherAssert.assertThat(context.getResponse().asString(), matchesJsonSchemaInClasspath(PRODUCTS_SCHEMA));
    }

    @Then("every product should have an id, name, price and brand")
    public void everyProductShouldHaveTheMainFields() {
        ProductsResponse body = ApiResponses.as(context.getResponse(), ProductsResponse.class);
        assertThat(body.products()).isNotEmpty();

        SoftAssertions softly = new SoftAssertions();
        body.products().forEach(product -> {
            softly.assertThat(product.id()).as("id of %s", product.name()).isPositive();
            softly.assertThat(product.name()).as("name of product %s", product.id()).isNotBlank();
            softly.assertThat(product.price()).as("price of %s", product.name()).startsWith("Rs. ");
            softly.assertThat(product.brand()).as("brand of %s", product.name()).isNotBlank();
        });
        softly.assertAll();
    }

    @Then("the brands list should include {string}")
    public void theBrandsListShouldInclude(String brand) {
        BrandsResponse body = ApiResponses.as(context.getResponse(), BrandsResponse.class);
        assertThat(body.brands()).extracting(BrandsResponse.Brand::brand).contains(brand);
    }

    @Then("the API results should include the product {string}")
    public void theApiResultsShouldIncludeTheProduct(String productName) {
        ProductsResponse body = ApiResponses.as(context.getResponse(), ProductsResponse.class);
        assertThat(body.products()).extracting(ProductsResponse.Product::name).contains(productName);
    }
}
