/*
package com.pepino.authservice.config;

import com.pepino.authservice.model.JwtInternalService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final JwtInternalService jwtInternalService;
    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public RequestInterceptor serviceTokenInterceptor() {
        return requestTemplate -> {
            String token = jwtInternalService.generateInternalServiceToken(serviceName);
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
*/
