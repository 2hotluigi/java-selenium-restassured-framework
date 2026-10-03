package com.luisabrego.automation.steps.api;

import com.luisabrego.automation.api.ApiResponses;
import com.luisabrego.automation.api.UserApi;
import com.luisabrego.automation.api.models.UserDetailsResponse;
import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.utils.TestDataFactory;
import com.luisabrego.automation.utils.UserData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;

public class UserApiSteps {

    private final TestContext context;

    public UserApiSteps(TestContext context) {
        this.context = context;
    }

    @When("I create a new account via the API")
    public void iCreateANewAccountViaTheApi() {
        UserData user = TestDataFactory.newUser();
        context.setUser(user);
        context.registerForCleanUp(user);
        context.setResponse(UserApi.createAccount(user));
    }

    @When("I verify the login with the account credentials")
    public void iVerifyTheLoginWithTheAccountCredentials() {
        UserData user = context.getUser();
        context.setResponse(UserApi.verifyLogin(user.email(), user.password()));
    }

    @When("I verify the login with email {string} and password {string}")
    public void iVerifyTheLoginWith(String email, String password) {
        context.setResponse(UserApi.verifyLogin(email, password));
    }

    @When("I verify the login without the email parameter")
    public void iVerifyTheLoginWithoutTheEmailParameter() {
        context.setResponse(UserApi.verifyLoginWithoutEmail("Any123!"));
    }

    @When("I send a DELETE request to verify login")
    public void iSendADeleteRequestToVerifyLogin() {
        context.setResponse(UserApi.deleteVerifyLogin());
    }

    @When("I request the user details by email")
    public void iRequestTheUserDetailsByEmail() {
        context.setResponse(UserApi.getUserDetailByEmail(context.getUser().email()));
    }

    @Then("the user details should match the created account")
    public void theUserDetailsShouldMatchTheCreatedAccount() {
        UserData expected = context.getUser();
        UserDetailsResponse.User actual =
                ApiResponses.as(context.getResponse(), UserDetailsResponse.class).user();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.name()).as("name").isEqualTo(expected.name());
        softly.assertThat(actual.email()).as("email").isEqualTo(expected.email());
        softly.assertThat(actual.firstName()).as("first name").isEqualTo(expected.firstName());
        softly.assertThat(actual.lastName()).as("last name").isEqualTo(expected.lastName());
        softly.assertThat(actual.city()).as("city").isEqualTo(expected.city());
        softly.assertThat(actual.country()).as("country").isEqualTo(expected.country());
        softly.assertAll();
    }

    @When("I delete the account via the API")
    public void iDeleteTheAccountViaTheApi() {
        UserData user = context.getUser();
        context.setResponse(UserApi.deleteAccount(user.email(), user.password()));
    }
}
