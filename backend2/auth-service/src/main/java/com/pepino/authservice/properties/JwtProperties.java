package com.pepino.authservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.token")
public class JwtProperties {
    private TokenConfig access;
    private TokenConfig refresh;
    private TokenConfig internalService;

    @Data
    public static class TokenConfig {
        private String privateKeyPath;
        private String publicKeyPath;
        private int expirationMs;
        private String name;
    }
}
