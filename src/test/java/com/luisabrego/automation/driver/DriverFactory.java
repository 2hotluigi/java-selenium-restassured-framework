package com.luisabrego.automation.driver;

import com.luisabrego.automation.config.ConfigReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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
     * Requests to these hosts are blocked so tests stay fast and deterministic.
     */
    private static final List<String> AD_HOSTS = List.of(
            "*.doubleclick.net",
            "*.googlesyndication.com",
            "*.googleadservices.com",
            "*.googletagservices.com",
            "*.adtrafficquality.google",
            "fundingchoicesmessages.google.com");

    /** Chromium browsers resolve the ad hosts to localhost. */
    private static final String CHROMIUM_HOST_RULES = AD_HOSTS.stream()
            .map(host -> "MAP " + host + " 127.0.0.1")
            .collect(Collectors.joining(", "));

    /**
     * Firefox has no host resolver rules, so a proxy auto-config script sends the ad hosts
     * to a closed local port and lets every other request go direct.
     */
    private static final String FIREFOX_AD_BLOCKING_PAC = "data:application/x-ns-proxy-autoconfig;base64,"
            + Base64.getEncoder().encodeToString((
            "function FindProxyForURL(url, host) {"
                    + AD_HOSTS.stream()
                    .map(host -> " if (shExpMatch(host, '" + host + "')) return 'PROXY 127.0.0.1:9';")
                    .collect(Collectors.joining())
                    + " return 'DIRECT'; }").getBytes(StandardCharsets.UTF_8));

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
                "--host-resolver-rules=" + CHROMIUM_HOST_RULES,
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
                "--host-resolver-rules=" + CHROMIUM_HOST_RULES,
                "--disable-notifications",
                "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addPreference("network.proxy.type", 2);
        options.addPreference("network.proxy.autoconfig_url", FIREFOX_AD_BLOCKING_PAC);
        options.addPreference("dom.webnotifications.enabled", false);
        options.addArguments("-width=1920", "-height=1080");
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }
}
