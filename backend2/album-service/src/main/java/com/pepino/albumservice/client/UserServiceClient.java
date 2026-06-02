package com.pepino.albumservice.client;

import com.pepino.albumservice.config.SecuredFeignClientsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", configuration = SecuredFeignClientsConfig.class)
public interface UserServiceClient {
    @GetMapping("/user/findByLogin/{login}")
    ResponseEntity<?> getUserByLogin(@PathVariable(value = "login") String login);

    @GetMapping("/user/getAll")
    ResponseEntity<?> getUsersByIds(@RequestParam List<UUID> ids);
}
