package com.pepino.userservice.client;

import com.pepino.userservice.config.SecuredFeignClientsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "album-service", configuration = SecuredFeignClientsConfig.class)
public interface AlbumServiceClient {
    @PostMapping("/album/creatorLogin/update")
    ResponseEntity<?> updateAlbumCreatorLogin(@RequestParam UUID creatorId, @RequestParam String newLogin);
}
