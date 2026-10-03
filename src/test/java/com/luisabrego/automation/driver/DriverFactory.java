package com.luisabrego.automation.driver;

import com.luisabrego.automation.config.ConfigReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.Locale;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Creates a WebDriver for the configured browser, locally or on a Selenium Grid.
 * Drivers are resolved automatically by Selenium Manager.
 */
public final class DriverFactory {

    /**
     * The demo site is full of ads that cover elements and add random "vignette" pages.
     * Chromium browsers resolve ad hosts to localhost so tests stay fast and deterministic.
     */
    private static final String BLOCKED_AD_HOSTS = String.join(", ",
            "MAP *.doubleclick.net 127.0.0.1",
            "MAP *.googlesyndication.com 127.0.0.1",
            "MAP *.googleadservices.com 127.0.0.1",
            "MAP *.googletagservices.com 127.0.0.1",
            "MAP *.adtrafficquality.google 127.0.0.1",
            "MAP fundingchoicesmessages.google.com 127.0.0.1");

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase(Locale.ROOT);
        boolean headless = ConfigReader.getBoolean("headless");
        String gridUrl = ConfigReader.get("grid.url", "");

        Capabilities options = switch (browser) {
            case "chrome" -> chromeOptions(headless);
            case "edge" -> edgeOptions(headless);
            case "firefox" -> firefoxOptions(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        WebDriver driver = gridUrl.isBlank() ? localDriver(browser, options) : remoteDriver(gridUrl, options);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds", 30)));
        return driver;
    }

    private static WebDriver localDriver(String browser, Capabilities options) {
        return switch (browser) {
            case "edge" -> new EdgeDriver((EdgeOptions) options);
            case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
            default -> new ChromeDriver((ChromeOptions) options);
        };
    }

    private static WebDriver remoteDriver(String gridUrl, Capabilities options) {
        try {
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url: " + gridUrl, e);
        }
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments(
                "--host-resolver-rules=" + BLOCKED_AD_HOSTS,
                "--disable-notifications",
                "--disable-search-engine-choice-screen",
                "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments(
                "--host-resolver-rules=" + BLOCKED_AD_HOSTS,
                "--disable-notifications",
                "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }
}
