package com.luisabrego.automation.reporting;

import com.luisabrego.automation.config.ConfigReader;
import com.luisabrego.automation.driver.CurrentDriver;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

/**
 * Attaches a screenshot to every passed Gherkin step that runs with an open browser, so a
 * manual review of the Allure report can confirm what each step really did.
 * Failed scenarios already get a screenshot in the {@code @After} hook.
 *
 * <p>It must be registered before the Allure plugin in {@code @CucumberOptions}: handlers run in
 * registration order, so the step is still open in Allure and the screenshot lands inside it.
 * An {@code @AfterStep} hook would run after Allure closed the step and show up as an extra step.
 *
 * <p>Enable or disable it with {@code screenshot.passed.steps} (config.properties, env or -D).
 */
public class StepScreenshotPlugin implements ConcurrentEventListener {

    private final boolean enabled = ConfigReader.getBoolean("screenshot.passed.steps");

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (enabled) {
            publisher.registerHandlerFor(TestStepFinished.class, this::attachScreenshot);
        }
    }

    private void attachScreenshot(TestStepFinished event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep)
                || event.getResult().getStatus() != Status.PASSED) {
            return;
        }
        CurrentDriver.get().ifPresent(driver -> {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
            } catch (WebDriverException ignored) {
                // A screenshot is evidence, not a check: it must never change the result of a step.
            }
        });
    }
}
