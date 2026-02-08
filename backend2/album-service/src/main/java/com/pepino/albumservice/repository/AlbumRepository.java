package com.pepino.albumservice.repository;

import com.pepino.albumservice.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
    List<Album> findAllByCreatorId(UUID creatorId);
    List<Album> findAllByIdIn(List<UUID> ids);
}
