package com.pepino.albumservice.controller;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.Photo;
import com.pepino.albumservice.service.AlbumMemberService;
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
    private final AlbumMemberService albumMemberService;
    private final PhotoService photoService;

    @PostMapping("/save")
    public ResponseEntity<?> saveAlbum(@RequestBody Album album) throws Exception {
        return ResponseEntity.status(201).body(albumService.saveAlbum(album)); //return dto only!!
    }

    @GetMapping("")
    public ResponseEntity<?> findById(@RequestParam UUID id) throws Exception {
        Optional<Album> albumOptional = Optional.ofNullable(albumService.getAlbumById(id));
        if (albumOptional.isPresent()) {
            return ResponseEntity.ok(albumOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get/all/created")
    public ResponseEntity<?> findAllByCreatorId(@RequestParam UUID creatorId) throws Exception {
        return ResponseEntity.status(200).body(albumService.getCreatedAlbums(creatorId));
    }

    @GetMapping("/get/all/added")
    public ResponseEntity<?> findAllAddedAlbums(@RequestParam UUID userId) throws Exception {
        return ResponseEntity.status(200).body(albumService.getAddedAlbums(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAlbum(@RequestParam UUID albumId, @RequestBody Album album) throws Exception {
        return ResponseEntity.status(200).body(albumService.updateAlbum(albumId, album));
    }

    @PostMapping("/save/photo")
    public ResponseEntity<?> saveAlbumPhoto(@RequestBody Photo photo) throws Exception {
        return ResponseEntity.status(200).body(photoService.savePhoto(photo));
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() throws Exception {
        return ResponseEntity.ok().body("pong");
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

    @GetMapping("/get/all/members")
    public ResponseEntity<?> getAllAlbumMembers(@RequestParam UUID albumId) throws Exception {
        return ResponseEntity.status(200).body(albumMemberService.getAllAlbumMembers(albumId));
    }
}
