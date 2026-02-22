package com.pepino.userservice.service.feign;

import com.pepino.userservice.client.AlbumServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumFeignRequestService {
    private final AlbumServiceClient albumServiceClient;

    public ResponseEntity<?> updateAlbumCreatorLogin(UUID creatorId, String newLogin) throws Exception {
        try {
            return albumServiceClient.updateAlbumCreatorLogin(creatorId, newLogin);
        } catch (Exception e) {
            log.error("Не удалось обновить логин создателя альбома для creatorId: " + creatorId, e);
            throw new Exception("Не удалось обновить логин создателя альбома", e);
        }
    }
}
