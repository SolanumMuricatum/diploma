package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.entity.Photo;
import com.pepino.albumservice.repository.PhotoRepository;
import com.pepino.albumservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public Photo savePhoto(Photo photo) {
        long amount = photoRepository.countByAlbumIdAndUserId(photo.getAlbumId(), photo.getUserId());
        if (amount >= 10) {
            throw new IllegalArgumentException("Превышен лимит фотографий в альбоме");
        }
        return photoRepository.save(photo);
    }

    @Override
    public List<Photo> getAllPhotos(UUID albumId, UUID userId) {
        return photoRepository.findAllByAlbumIdAndUserId(albumId, userId);
    }

    @Override
    public void deletePhoto(UUID photoId) {
        if (!photoRepository.existsById(photoId)) {
            throw new IllegalArgumentException("Фото не найдено");
        }
        photoRepository.deleteById(photoId);
    }
}
