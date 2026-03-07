package com.pepino.userservice.service.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepino.userservice.client.AlbumServiceClient;
import com.pepino.userservice.dto.MemberInvitationDto;
import com.pepino.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumFeignRequestService {
    private final AlbumServiceClient albumServiceClient;
    private final ObjectMapper objectMapper;

    public void updateAlbumCreatorLogin(UUID creatorId, String newLogin) throws Exception {
        try {
            ResponseEntity<?> response = albumServiceClient.updateAlbumCreatorLogin(creatorId, newLogin);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Error while updating user's login");
            }
        } catch (Exception e) {
            log.error("Не удалось обновить логин создателя альбома для creatorId: " + creatorId, e);
            throw new Exception("Не удалось обновить логин создателя альбома", e);
        }
    }

    public List<User> getAllUsersInAlbum(UUID albumId) throws Exception {
        try {
            ResponseEntity<?> response = albumServiceClient.getAllUsersInAlbum(albumId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Error while fetching users");
            }

            return objectMapper.convertValue(response.getBody(), new TypeReference<List<User>>() {});

        } catch (Exception e) {
            log.error("Не удалось получить пользователей из текущего альбома: " + albumId, e);
            throw new Exception("Не удалось получить пользователей из текущего альбома: " + albumId, e);
        }
    }

    public List<UUID> getAllUsersIdInAlbumInvitations(UUID albumId) throws Exception {
        try {
            ResponseEntity<?> response = albumServiceClient.getAllAlbumInvitations(albumId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Error while fetching users");
            }

            return objectMapper.convertValue(
                    response.getBody(),
                    new TypeReference<List<MemberInvitationDto>>() {}
            ).stream()
                    .map(el -> el.getId().getUserId())
                    .toList();

        } catch (Exception e) {
            log.error("Не удалось получить пользователей из приглашений в фотоальбом: " + albumId, e);
            throw new Exception("Не удалось получить пользователей из приглашений в фотоальбом: " + albumId, e);
        }
    }
}
