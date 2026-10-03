package com.luisabrego.automation.driver;

import java.util.Optional;
import org.openqa.selenium.WebDriver;

/**
 * The browser of the scenario running on the current thread. Cucumber plugins live outside
 * the scenario's PicoContainer, so this is how they reach the driver. Every scenario runs on
 * a single thread, which keeps parallel scenarios isolated.
 */
public final class CurrentDriver {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private CurrentDriver() {
    }

    public static Optional<WebDriver> get() {
        return Optional.ofNullable(DRIVER.get());
    }

    public static void set(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void clear() {
        DRIVER.remove();
    }
}
