package com.luisabrego.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Reads configuration with this precedence:
 * system property (-Dkey=value) &gt; environment variable (KEY_NAME) &gt; config.properties.
 */
public final class ConfigReader {

    private static final String FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        String envValue = System.getenv(key.toUpperCase(Locale.ROOT).replace('.', '_'));
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }
        String fileValue = PROPERTIES.getProperty(key);
        return fileValue == null ? null : fileValue.trim();
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, "false"));
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }

    private static Properties load() {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(FILE)) {
            if (in == null) {
                throw new IllegalStateException(FILE + " was not found on the classpath");
            }
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + FILE, e);
        }
    }
}
