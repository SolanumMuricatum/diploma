/*
package com.pepino.authservice.client;

import com.pepino.authservice.config.FeignConfig;
import com.pepino.authservice.dto.UserAuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/user/findByLogin/{login}")
    ResponseEntity<UserAuthDto> getUserByLogin(@PathVariable(value = "login") String login);
}
*/
