package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
}
