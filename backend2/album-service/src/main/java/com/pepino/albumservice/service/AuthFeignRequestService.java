package com.pepino.albumservice.service;

import com.pepino.albumservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFeignRequestService {
    private final AuthServiceClient authServiceClient;

    @Value("${spring.application.name}")
    private String serviceName;

    public String getInternalServiceToken() throws Exception {
        try {
            ResponseEntity<String> response = authServiceClient.getInternalServiceToken(serviceName);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new Exception("Error while fetching internal service token");
            }

            return response.getBody();

        } catch (feign.FeignException.NotFound e) {
            throw new Exception("Internal service token not found");
        } catch (Exception e) {
            throw new Exception("Error while fetching internal service token: " + e.getMessage());
        }
    }

    public String getInternalServicePublicKey() throws Exception {
        try {
            ResponseEntity<String> response = authServiceClient.getInternalServicePublicKey();

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new Exception("Error while fetching public key");
            }

            return response.getBody();

        } catch (feign.FeignException.NotFound e) {
            throw new Exception("Public key not found");
        } catch (Exception e) {
            throw new Exception("Error while fetching public key: " + e.getMessage());
        }
    }

}
