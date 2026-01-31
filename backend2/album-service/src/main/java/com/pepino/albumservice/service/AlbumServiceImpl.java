package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final UserFeignRequestService userFeignRequestService;

    @Override
    public Album saveAlbum(Album album) throws Exception {
        if (!userFeignRequestService.isUserExists(album.getCreatorLoginSnapshot())) {
            throw new Exception("Пользователя с таким логином не найдено");
        }
        album.setStartDate(LocalDate.now());
        album.setEndDate(LocalDate.now().plusMonths(1));
        albumRepository.save(album);
        return album;
    }

    @Override
    public Album getAlbum(UUID id) throws Exception {
        return albumRepository.findById(id).orElseThrow(() -> new Exception("Альбом с таким ID не найден"));
    }

    @Override
    public List<Album> getAlbumsByCreatorId(UUID creatorId) throws Exception {
        List<Album> albums = albumRepository.findAllByCreatorId(creatorId);
        if (albums.isEmpty()) {
            throw new IllegalArgumentException("Альбомы с таким ID создателя не найдены");
        }

        return albums;
    }

    public Album updateAlbum(UUID id, Album album) throws Exception {
        Album existingAlbum = albumRepository.findById(id).orElseThrow(() -> new Exception("Альбом с таким ID не найден"));
        existingAlbum.setName(album.getName());
        existingAlbum.setBackground(album.getBackground());
        existingAlbum.setTextColor(album.getTextColor());
        existingAlbum.setTextFont(album.getTextFont());
        existingAlbum.setTextSize(album.getTextSize());
        albumRepository.save(existingAlbum);
        return existingAlbum;
    }
}
