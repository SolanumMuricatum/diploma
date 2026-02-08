package com.pepino.authservice.service;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.dto.UserAuthDto;
import com.pepino.authservice.model.LoginRequest;
import com.pepino.authservice.service.impl.AuthServiceImpl;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

public interface AuthService {

    Mono<?> login(LoginRequest loginRequest, ServerWebExchange exchange) throws Exception;
/*    boolean isUserExist(String email);*/
    Mono<UserDetailsImpl> authCheck() throws Exception;
    Mono<Void> authDelete(ServerWebExchange exchange) throws Exception;
    String getInternalServiceToken(String serviceName) throws Exception;
    String getInternalServicePublicKey() throws Exception;
    String getAccessTokenPublicKey() throws Exception;
}