package com.luisabrego.automation.steps.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.luisabrego.automation.api.ApiResponses;
import com.luisabrego.automation.api.UserApi;
import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.utils.TestDataFactory;
import com.luisabrego.automation.utils.UserData;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

/** Preconditions created through the API so UI scenarios start fast and independent. */
public class UserSetupSteps {

    private final TestContext context;

    public UserSetupSteps(TestContext context) {
        this.context = context;
    }

    @Given("a registered user exists")
    public void aRegisteredUserExists() {
        UserData user = TestDataFactory.newUser();
        context.registerForCleanUp(user);

        Response response = UserApi.createAccount(user);
        assertThat(ApiResponses.responseCode(response))
                .as("Precondition failed: could not create the test user via API. Body: %s", response.asString())
                .isEqualTo(201);

        context.setUser(user);
    }
}
