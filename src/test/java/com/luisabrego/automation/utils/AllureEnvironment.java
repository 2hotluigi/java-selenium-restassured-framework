package com.luisabrego.automation.utils;

import com.luisabrego.automation.config.ConfigReader;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Writes environment.properties into the Allure results folder, so the report shows
 * where and how the run was executed (Environment widget on the Overview page).
 */
public final class AllureEnvironment {

    private static final String RESULTS_DIRECTORY = "target/allure-results";

    private AllureEnvironment() {
    }

    public static void write() {
        String gridUrl = ConfigReader.get("grid.url");
        String tags = System.getProperty("cucumber.filter.tags");

        Properties environment = new Properties();
        environment.setProperty("Browser", ConfigReader.get("browser", "chrome"));
        environment.setProperty("Headless", String.valueOf(ConfigReader.getBoolean("headless")));
        environment.setProperty("Execution", gridUrl == null || gridUrl.isBlank() ? "Local" : "Selenium Grid");
        environment.setProperty("Base URL", ConfigReader.get("base.url"));
        environment.setProperty("API base URL", ConfigReader.get("api.base.url"));
        environment.setProperty("Tags", tags == null || tags.isBlank() ? "All scenarios" : tags);
        environment.setProperty("Java", System.getProperty("java.version"));
        environment.setProperty("OS", System.getProperty("os.name"));

        Path directory = Path.of(System.getProperty("allure.results.directory", RESULTS_DIRECTORY));
        try {
            Files.createDirectories(directory);
            try (OutputStream out = Files.newOutputStream(directory.resolve("environment.properties"))) {
                environment.store(out, null);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write the Allure environment file", e);
        }
    }
}
