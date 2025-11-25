package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Album;

import java.util.UUID;

public interface AlbumService {
    public Album saveAlbum(Album album) throws Exception;
    public Album getAlbum(UUID id) throws Exception;
}
