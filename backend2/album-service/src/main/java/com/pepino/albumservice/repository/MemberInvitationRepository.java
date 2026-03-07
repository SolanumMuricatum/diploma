package com.pepino.albumservice.repository;

import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.entity.MemberInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface MemberInvitationRepository extends JpaRepository<MemberInvitation, AlbumMemberId> {
    List<MemberInvitation> findAllByIdUserId(UUID userId);
    List<MemberInvitation> findAllByIdAlbumId(UUID albumId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE MemberInvitation mi SET mi.accepted = null WHERE mi.id = :id")
    void resetInvitationStatus(@Param("id") AlbumMemberId id);
}
