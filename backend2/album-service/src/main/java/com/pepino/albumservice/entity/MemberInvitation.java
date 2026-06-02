package com.pepino.albumservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_invitation")
public class MemberInvitation {
    @EmbeddedId
    private AlbumMemberId id;
    @Column(name = "is_accepted")
    private Boolean accepted;
    @Column(name = "creator_login_snapshot")
    private String creatorLoginSnapshot;
    @Column(name = "album_name")
    private String albumName;
}
