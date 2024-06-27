package com.example.organization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtConverterProperties {
    private String resourceId;
    private String principalAttribute;
}
