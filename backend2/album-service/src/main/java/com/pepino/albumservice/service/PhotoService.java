package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Photo;

import java.util.List;
import java.util.UUID;

public interface PhotoService {
    Photo savePhoto(Photo photo);
    List<Photo> getAllPhotos(UUID albumId, UUID userId);
    void deletePhoto(UUID photoId);

}
