package com.martishyn.licenseservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "example")
@Configuration
public class ServiceConfig {
    private String property;

    public String getProperty() {
        return property;
    }
}

