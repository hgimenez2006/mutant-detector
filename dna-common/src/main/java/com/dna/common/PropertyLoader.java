package com.dna.common;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    public static Properties getProperties(AbstractModule module){
        try{
            Properties properties = new Properties();
            properties.load(module.getClass().getClassLoader().getResourceAsStream("app.properties"));
            return properties;
        } catch (IOException e){
            throw new RuntimeException("Could not load properties file");
        }
    }
}
