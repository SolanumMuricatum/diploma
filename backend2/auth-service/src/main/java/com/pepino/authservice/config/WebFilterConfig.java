package com.pepino.authservice.config;

import com.pepino.authservice.model.JwtInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

/*@Configuration
@RequiredArgsConstructor
public class WebFilterConfig {

    private final JwtInternalService jwtInternalService;
    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public WebFilter addInternalServiceTokenFilter() {
        return (exchange, chain) -> {
            if (exchange.getRequest().getURI().getPath().startsWith("/user")) {
                String token = jwtInternalService.generateInternalServiceToken(serviceName);
                exchange.getAttributes().put("X-Internal-Token", token);
            }
            return chain.filter(exchange);
        };
    }

}*/
