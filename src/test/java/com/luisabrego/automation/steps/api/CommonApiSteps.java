package com.luisabrego.automation.steps.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.luisabrego.automation.api.ApiResponses;
import com.luisabrego.automation.context.TestContext;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

public class CommonApiSteps {

    private final TestContext context;

    public CommonApiSteps(TestContext context) {
        this.context = context;
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expected) {
        Response response = context.getResponse();
        // The HTTP status is always 200; the meaningful code lives in the body.
        assertThat(response.statusCode()).as("HTTP status").isEqualTo(200);
        assertThat(ApiResponses.responseCode(response))
                .as("responseCode in body: %s", response.asString())
                .isEqualTo(expected);
    }

    @Then("the response message should be {string}")
    public void theResponseMessageShouldBe(String expected) {
        assertThat(ApiResponses.message(context.getResponse())).isEqualTo(expected);
    }
}
