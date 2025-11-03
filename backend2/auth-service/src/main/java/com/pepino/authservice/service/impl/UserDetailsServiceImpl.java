package com.pepino.authservice.service.impl;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.dto.UserAuthDto;
import com.pepino.authservice.model.JwtInternalService;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final WebClient.Builder webClientBuilder;

    private final JwtInternalService jwtInternalService;
    @Value("${spring.application.name}")
    private String serviceName;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        String token = jwtInternalService.generateInternalServiceToken(serviceName);

        return webClientBuilder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .get()
                .uri("http://user-service/user/findByLogin/{login}", username)
                .retrieve()
                .bodyToMono(UserAuthDto.class)
                .retry(0)
                // Преобразуем UserAuthDto -> UserDetailsImpl -> UserDetails
                .map(UserDetailsImpl::build)
                .map(userDetails -> (UserDetails) userDetails)  // <-- Явное приведение
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException.NotFound) {
                        return Mono.error(new UsernameNotFoundException(
                                "User '%s' not found".formatted(username)
                        ));
                    }
                    return Mono.error(new UsernameNotFoundException(
                            "Error fetching user: " + e.getMessage()
                    ));
                });
    }
}

