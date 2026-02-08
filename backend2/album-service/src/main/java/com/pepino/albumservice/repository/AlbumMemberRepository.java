package com.pepino.albumservice.repository;

import com.pepino.albumservice.entity.AlbumMember;
import com.pepino.albumservice.entity.AlbumMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlbumMemberRepository extends JpaRepository<AlbumMember, AlbumMemberId> {
    List<AlbumMember> findAllByIdAlbumId(UUID albumId);
    List<AlbumMember> findAllByIdUserIdAndCreatorTrue(UUID userId);
    List<AlbumMember> findAllByIdUserIdAndCreatorFalse(UUID userId);
}
