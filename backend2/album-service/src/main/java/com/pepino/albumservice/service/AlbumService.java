package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumService {
    public Album saveAlbum(Album album) throws Exception;
    public Album getAlbumById(UUID id) throws Exception;
    List<Album> getCreatedAlbums(UUID creatorId) throws Exception;
    List<Album> getAddedAlbums(UUID userId) throws Exception;
    public Album updateAlbum(UUID id, Album album) throws Exception;
    public Album updateAlbumCreatorLogin(UUID creatorId, String newLogin) throws Exception;
}
