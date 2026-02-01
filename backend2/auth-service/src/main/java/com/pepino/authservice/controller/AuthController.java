package com.pepino.authservice.controller;

import com.pepino.authservice.model.LoginRequest;
import com.pepino.authservice.service.AuthService;
import com.pepino.authservice.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(
            @RequestBody LoginRequest loginRequest,
            ServerWebExchange exchange
    ) throws Exception {
        return authService.login(loginRequest, exchange)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/check")
    public ResponseEntity<?> authCheck() throws Exception {
        return ResponseEntity.ok(authService.authCheck());
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) throws Exception {
        return authService.authDelete(exchange)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() throws Exception {
        return ResponseEntity.ok().body("pong");
    }

    //защищённые эндпоинты (я ж не девопс чтобы это настраивать)
    @GetMapping("/internal-service/token")
    public ResponseEntity<?> getInternalToken(@RequestParam("service-name") String serviceName) throws Exception {
        return ResponseEntity.ok(authService.getInternalServiceToken(serviceName));
    }

    @GetMapping("/internal-service/public-key")
    public ResponseEntity<?> getInternalPublicKey() throws Exception {
        return ResponseEntity.ok(authService.getInternalServicePublicKey());
    }

    @GetMapping("/access-token/public-key")
    public ResponseEntity<?> getAccessTokenPublicKey() throws Exception {
        return ResponseEntity.ok(authService.getAccessTokenPublicKey());
    }
}