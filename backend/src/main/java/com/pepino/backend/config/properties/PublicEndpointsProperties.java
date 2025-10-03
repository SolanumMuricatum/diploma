package com.pepino.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.endpoints")
public class PublicEndpointsProperties {
    private String login;
    private String registration;
}