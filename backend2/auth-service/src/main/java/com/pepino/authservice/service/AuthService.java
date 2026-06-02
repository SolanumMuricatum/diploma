package com.pepino.authservice.service;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.model.LoginRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<?> login(LoginRequest loginRequest, ServerWebExchange exchange) throws Exception;
    Mono<UserDetailsImpl> authCheck() throws Exception;
    Mono<Void> authDelete(ServerWebExchange exchange) throws Exception;
    String getInternalServiceToken(String serviceName) throws Exception;
    String getInternalServicePublicKey() throws Exception;
    String getAccessTokenPublicKey() throws Exception;
}