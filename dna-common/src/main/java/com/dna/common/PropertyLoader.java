package com.dna.common;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    public static Properties getProperties(AbstractModule module){
        try{
            Properties properties = new Properties();
            properties.load(module.getClass().getClassLoader().getResourceAsStream("app.properties"));

            // override properties values with environment variable if exists
            properties.stringPropertyNames().stream()
                    .forEach(property -> {
                        String systemProperty = System.getenv(property);
                        if (systemProperty != null){
                            properties.setProperty(property, systemProperty);
                        }
                    });

            return properties;

        } catch (IOException e){
            throw new RuntimeException("Error loading properties", e);
        }
    }
}
