package com.pepino.authservice.service.impl;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.dto.UserAuthDto;
import com.pepino.authservice.model.JwtAccess;
import com.pepino.authservice.model.JwtInternalService;
import com.pepino.authservice.model.LoginRequest;
import com.pepino.authservice.service.AuthService;
import com.pepino.authservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final TokenService tokenService;
/*    private final UserServiceClient userServiceClient;*/
    private final JwtInternalService jwtInternalService;
    private final JwtAccess jwtAccessToken;

    @Override
    public Mono<Void> login(@RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()))
                .flatMap(authentication -> {
                    // Создаём куки с токенами
                    exchange.getResponse().addCookie(tokenService.createAccessTokenCookie(authentication));
                    exchange.getResponse().addCookie(tokenService.createRefreshTokenCookie(authentication));

                    // Ставим Authentication в реактивный контекст
                    return Mono.just(authentication)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .then(); // Возвращаем Mono<Void> для контроллера
    }


/*    @Override
    public boolean isUserExist(String login) {
        return userServiceClient.getUserByLogin(login).getStatusCode().is2xxSuccessful();
    }*/

    @Override
    public Mono<UserDetailsImpl> authCheck() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .flatMap(principal -> {
                    if (principal instanceof UserDetailsImpl userDetailsImpl) {
                        return Mono.just(userDetailsImpl);
                    } else {
                        return Mono.error(new Exception("User not found"));
                    }
                });
    }

    @Override
    public Mono<Void> authDelete(ServerWebExchange exchange){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication) // <— используем контекст из Mono
                .map(Authentication::getPrincipal)
                .flatMap(principal -> {
                    if (principal instanceof UserDetailsImpl) {
                        exchange.getResponse().addCookie(tokenService.deleteAccessTokenCookie());
                        exchange.getResponse().addCookie(tokenService.deleteRefreshTokenCookie());
                        return Mono.just(ReactiveSecurityContextHolder.clearContext()).then();
                    } else {
                        return Mono.error(new Exception("Something went wrong while logout :("));
                    }
                });
    }

    @Override
    public String getInternalServiceToken(String serviceName) {
        return jwtInternalService.generateInternalServiceToken(serviceName);
    }

    @Override
    public String getInternalServicePublicKey() {
        return jwtInternalService.getPublicKey();
    }

    @Override
    public String getAccessTokenPublicKey() {
        return jwtAccessToken.getPublicKey();
    }
}