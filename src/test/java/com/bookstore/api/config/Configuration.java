package com.bookstore.api.config;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Configuration.class.getClassLoader().getResourceAsStream("config/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static String getBooksEndpoint() {
        return properties.getProperty("api.books.endpoint");
    }

    public static String getAuthorsEndpoint() {
        return properties.getProperty("api.authors.endpoint");
    }
}