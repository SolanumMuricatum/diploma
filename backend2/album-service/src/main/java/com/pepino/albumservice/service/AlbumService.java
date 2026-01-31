package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumService {
    public Album saveAlbum(Album album) throws Exception;
    public Album getAlbum(UUID id) throws Exception;
    List<Album> getAlbumsByCreatorId(UUID creatorId) throws Exception;
    public Album updateAlbum(UUID id, Album album) throws Exception;
}
