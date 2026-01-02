package com.example.urlshortener.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ApplicationConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load application.properties", e);
        }
    }

    public static long defaultTtlSeconds() {
        return Long.parseLong(PROPS.getProperty("link.defaultTtlSeconds", "86400"));
    }

    public static long defaultMaxClicks() {
        return Long.parseLong(PROPS.getProperty("link.defaultMaxClicks", "100"));
    }

    public static long cleanupIntervalSeconds() {
        return Long.parseLong(PROPS.getProperty("cleanup.intervalSeconds", "60"));
    }

    private ApplicationConfig() {}
}
