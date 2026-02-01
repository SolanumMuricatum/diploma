package com.pepino.albumservice.repository;

import com.pepino.albumservice.entity.AlbumMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlbumMemberRepository extends JpaRepository<AlbumMember, UUID> {
}
