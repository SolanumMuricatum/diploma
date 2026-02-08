package com.pepino.albumservice.service.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepino.albumservice.client.UserServiceClient;
import com.pepino.albumservice.dto.UserDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeignRequestService {
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    public boolean isUserExists(String userLogin) throws Exception {
        boolean result = false;
        try {
            System.out.println("isUserExists: " + userLogin);
            ResponseEntity<?> response = userServiceClient.getUserByLogin(userLogin);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new Exception("Error while fetching user");
            }

            result = true;

        } catch (FeignException.NotFound e) {
            throw new Exception("Something went wrong while fetching user");
        }

        System.out.println("isUserExists: " + result);
        return result;
    }

    public List<UserDto> getUsersByIds(List<UUID> ids) {
        List<UserDto> userDtos = new ArrayList<>();
        try {
            ResponseEntity<?> response = userServiceClient.getUsersByIds(ids);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Error while fetching users");
            }

            userDtos = objectMapper.convertValue(response.getBody(), new TypeReference<List<UserDto>>() {});

        } catch (FeignException.NotFound e) {
            log.error("Something went wrong while fetching user");
        }

        return userDtos;
    }
}
