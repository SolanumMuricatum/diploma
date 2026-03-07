package com.pepino.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationDto {
    private AlbumMemberId id;
    private Boolean accepted;
    private String creatorLoginSnapshot;
    private String albumName;

    @Data
    public static class AlbumMemberId {
        private UUID userId;
        private UUID albumId;
    }
}
