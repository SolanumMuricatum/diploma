package com.pepino.userservice.config;

import com.pepino.userservice.service.feign.AuthFeignRequestService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecuredFeignClientsConfig {
    private final AuthFeignRequestService authFeignRequestService;

    @Bean
    public RequestInterceptor serviceTokenInterceptor() {
        return requestTemplate -> {
            try {
                requestTemplate.header("Authorization", "Bearer " + authFeignRequestService.getInternalServiceToken());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
