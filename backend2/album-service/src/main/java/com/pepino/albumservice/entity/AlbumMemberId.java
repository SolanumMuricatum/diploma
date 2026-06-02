package com.pepino.albumservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@Embeddable
public class AlbumMemberId implements Serializable {
    private UUID albumId;
    private UUID userId;

    public AlbumMemberId() {
    }

    public AlbumMemberId(UUID albumId, UUID userId) {
        this.albumId = albumId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumMemberId that = (AlbumMemberId) o;
        return Objects.equals(albumId, that.albumId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, userId);
    }
}
