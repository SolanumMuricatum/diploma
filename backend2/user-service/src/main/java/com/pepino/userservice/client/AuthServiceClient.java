package com.pepino.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.PublicKey;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @GetMapping("/auth/internal-service/public-key")
    ResponseEntity<String> getInternalServicePublicKey();
}
