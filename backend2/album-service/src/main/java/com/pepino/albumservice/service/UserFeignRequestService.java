package com.pepino.albumservice.service;

import com.pepino.albumservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFeignRequestService {
    private final UserServiceClient userServiceClient;

    public boolean isUserExists(String userLogin) throws Exception {
        boolean result = false;
        try {
            System.out.println("isUserExists: " + userLogin);
            ResponseEntity<?> response = userServiceClient.getUserByLogin(userLogin);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("syka tyt ypali");
                throw new Exception("Error while fetching user");
            }

            result = true;

        } catch (feign.FeignException.NotFound e) {
            throw new Exception("Something went wrong while fetching user");
        }

        System.out.println("isUserExists: " + result);
        return result;
    }
}
