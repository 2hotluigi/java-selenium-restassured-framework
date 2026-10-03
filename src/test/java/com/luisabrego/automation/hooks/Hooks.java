package com.luisabrego.automation.hooks;

import com.luisabrego.automation.api.UserApi;
import com.luisabrego.automation.config.ConfigReader;
import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.utils.UserData;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.util.ResultsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    /**
     * Groups the report by layer and browser (Suites tab: "API", "UI · Chrome", "UI · Firefox"...).
     * UI scenarios run once per browser in CI, so the browser is also part of their history id;
     * otherwise Allure would merge the runs of the same scenario as retries.
     */
    @Before(order = 0)
    public void groupInReport(Scenario scenario) {
        boolean ui = scenario.getSourceTagNames().contains("@ui");
        String browser = ConfigReader.get("browser", "chrome").toLowerCase(Locale.ROOT);
        String parentSuite = ui ? "UI · " + Character.toUpperCase(browser.charAt(0)) + browser.substring(1) : "API";

        // Inside a hook the current Allure item is the hook itself, so the scenario is updated by id.
        Allure.getLifecycle().updateTestCase(scenario.getId(), result -> {
            result.getLabels().add(ResultsUtils.createParentSuiteLabel(parentSuite));
            if (ui) {
                List<Parameter> parameters = new ArrayList<>(result.getParameters());
                parameters.add(new Parameter().setName("Browser").setValue(browser));
                result.setParameters(parameters);
                result.setHistoryId(ResultsUtils.md5(result.getHistoryId() + ":" + browser));
            }
        });
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
