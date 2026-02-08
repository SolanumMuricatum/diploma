package com.pepino.albumservice.service;

import com.pepino.albumservice.dto.UserDto;
import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.AlbumMember;

import java.util.List;
import java.util.UUID;

public interface AlbumMemberService {
    AlbumMember saveAlbumMember(Album album);
    List<UserDto> getAllAlbumMembers(UUID albumId);
    List<UUID> getAllCreatedAlbums(UUID creatorId);
    List<UUID> getAllAddedAlbums(UUID userId);
}