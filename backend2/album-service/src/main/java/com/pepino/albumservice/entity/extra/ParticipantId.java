package com.pepino.albumservice.entity.extra;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ParticipantId implements Serializable {

    private UUID albumId;
    private UUID userId;

    public ParticipantId() {
    }

    public ParticipantId(UUID albumId, UUID userId) {
        this.albumId = albumId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantId that = (ParticipantId) o;
        return Objects.equals(albumId, that.albumId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, userId);
    }
}
