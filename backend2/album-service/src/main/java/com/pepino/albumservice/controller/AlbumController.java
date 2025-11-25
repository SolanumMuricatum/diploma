package com.pepino.albumservice.controller;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @PostMapping("/save")
    public ResponseEntity<?> saveAlbum(@RequestBody Album album) throws Exception {
        return ResponseEntity.status(201).body(albumService.saveAlbum(album)); //return dto only!!
    }

    @GetMapping("")
    public ResponseEntity<?> findByLogin(@RequestParam UUID id) throws Exception {
        Optional<Album> albumOptional = Optional.ofNullable(albumService.getAlbum(id));
        if (albumOptional.isPresent()) {
            return ResponseEntity.ok(albumOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
