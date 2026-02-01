package com.pepino.albumservice.controller;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.Photo;
import com.pepino.albumservice.service.AlbumService;
import com.pepino.albumservice.service.PhotoService;
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
    private final PhotoService photoService;

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

    @GetMapping("/getAll")
    public ResponseEntity<?> findAllByCreatorId(@RequestParam UUID creatorId) throws Exception {
        return ResponseEntity.status(200).body(albumService.getAlbumsByCreatorId(creatorId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAlbum(@RequestParam UUID albumId, @RequestBody Album album) throws Exception {
        return ResponseEntity.status(200).body(albumService.updateAlbum(albumId, album));
    }

    @PostMapping("/save/photo")
    public ResponseEntity<?> saveAlbumPhoto(@RequestBody Photo photo) throws Exception {
        return ResponseEntity.status(200).body(photoService.savePhoto(photo));
    }

    @GetMapping("/get/all/photo")
    public ResponseEntity<?> getAllPhotos(@RequestParam UUID albumId, @RequestParam UUID userId) throws Exception {
        return ResponseEntity.status(200).body(photoService.getAllPhotos(albumId, userId));
    }

    @DeleteMapping("/delete/photo")
    public ResponseEntity<?> deletePhoto(@RequestParam UUID photoId) throws Exception {
        photoService.deletePhoto(photoId);
        return ResponseEntity.status(200).build();
    }
}
