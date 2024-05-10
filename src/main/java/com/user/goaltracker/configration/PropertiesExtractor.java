package com.user.goaltracker.configration;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesExtractor {
    private static Properties properties;
    static {
        properties = new Properties();
        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(in);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
