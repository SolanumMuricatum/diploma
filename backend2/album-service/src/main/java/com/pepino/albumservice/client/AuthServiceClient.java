package com.pepino.albumservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @GetMapping("/auth/internal-service/public-key")
    ResponseEntity<String> getInternalServicePublicKey();

    @GetMapping("/auth/internal-service/token")
    ResponseEntity<String> getInternalServiceToken(@RequestParam("service-name") String serviceName);

    @GetMapping("/auth/access-token/public-key")
    ResponseEntity<String> getAccessTokenPublicKey();
}
