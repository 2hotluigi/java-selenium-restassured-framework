package com.luisabrego.automation.context;

import com.luisabrego.automation.driver.DriverFactory;
import com.luisabrego.automation.utils.UserData;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;

/**
 * Scenario-scoped state shared between step definitions through PicoContainer.
 * A new instance is created for every scenario, which keeps parallel runs isolated.
 */
public class TestContext {

    private WebDriver driver;
    private Response response;
    private UserData user;
    private final List<UserData> usersToCleanUp = new ArrayList<>();

    /** The browser is only started when a step actually needs it (API scenarios never open one). */
    public WebDriver getDriver() {
        if (driver == null) {
            driver = DriverFactory.createDriver();
        }
        return driver;
    }

    public boolean hasDriver() {
        return driver != null;
    }

    public void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }

    public Response getResponse() {
        if (response == null) {
            throw new IllegalStateException("No API response stored yet. Call an endpoint first.");
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public UserData getUser() {
        if (user == null) {
            throw new IllegalStateException("No user in the scenario context yet.");
        }
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public void registerForCleanUp(UserData user) {
        usersToCleanUp.add(user);
    }

    public List<UserData> getUsersToCleanUp() {
        return List.copyOf(usersToCleanUp);
    }
}
