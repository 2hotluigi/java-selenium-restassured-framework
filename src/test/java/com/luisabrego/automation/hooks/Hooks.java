package com.luisabrego.automation.hooks;

import com.luisabrego.automation.api.UserApi;
import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.utils.UserData;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    /** Attaches a screenshot to the Allure report when a UI scenario fails, then closes the browser. */
    @After(order = 100)
    public void closeBrowser(Scenario scenario) {
        if (!context.hasDriver()) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on failure");
            }
        } finally {
            context.quitDriver();
        }
    }

    /** Deletes every account created by the scenario, even if it failed halfway. */
    @After(order = 10)
    public void deleteTestUsers() {
        for (UserData user : context.getUsersToCleanUp()) {
            try {
                UserApi.deleteAccount(user.email(), user.password());
            } catch (RuntimeException ignored) {
                // Clean-up must never hide the real result of the scenario.
            }
        }
    }
}
