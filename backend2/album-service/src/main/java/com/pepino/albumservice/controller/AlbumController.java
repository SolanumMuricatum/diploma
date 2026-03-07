package com.pepino.albumservice.controller;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.entity.MemberInvitation;
import com.pepino.albumservice.entity.Photo;
import com.pepino.albumservice.service.AlbumMemberService;
import com.pepino.albumservice.service.AlbumService;
import com.pepino.albumservice.service.MemberInvitationService;
import com.pepino.albumservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AlbumMemberService albumMemberService;
    private final PhotoService photoService;
    private final MemberInvitationService memberInvitationService;

    @PostMapping("/save")
    public ResponseEntity<?> saveAlbum(@RequestBody Album album) throws Exception {
        return ResponseEntity.status(201).body(albumService.saveAlbum(album));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAlbum(@RequestBody Map<String, Object> payload) throws Exception {
        String albumId = (String) payload.get("albumId");
        String userId = (String) payload.get("userId");

        albumService.deleteAlbum(UUID.fromString(albumId), UUID.fromString(userId));
        return ResponseEntity.status(204).build();
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

    @GetMapping("/get/members")
    public ResponseEntity<?> getAlbumMembersWithoutTheCreator(@RequestParam UUID albumId) throws Exception {
        return ResponseEntity.status(200).body(albumMemberService.getAlbumMembersWithoutTheCreator(albumId));
    }

    @PostMapping("/creatorLogin/update")
    public ResponseEntity<?> updateAlbumCreatorLogin(@RequestParam UUID creatorId, @RequestParam String newLogin) throws Exception {
        return ResponseEntity.status(200).body(albumService.updateAlbumCreatorLogin(creatorId, newLogin));
    }

    @DeleteMapping("/added/leave")
    public ResponseEntity<?> leaveAddedAlbum(@RequestBody Map<String, Object> payload) throws Exception {
        String albumId = (String) payload.get("albumId");
        String userId = (String) payload.get("userId");
        albumMemberService.leaveAddedAlbum(UUID.fromString(albumId), UUID.fromString(userId));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/get/all/album/invitations")
    public ResponseEntity<?> getAllAlbumInvitations(@RequestParam UUID albumId) {
        return ResponseEntity.status(200).body(memberInvitationService.findAllInvitationsForAlbum(albumId));
    }

    @GetMapping("/get/all/user/invitations")
    public ResponseEntity<?> getAllUserInvitations(@RequestParam UUID userId) {
        return ResponseEntity.status(200).body(memberInvitationService.findAllInvitationsForUser(userId));
    }

    @PostMapping("/save/invitation")
    public ResponseEntity<?> saveInvitation(@RequestBody Map<String, Object> payload) {
        String albumId = (String) payload.get("albumId");
        String userId = (String) payload.get("userId");
        String creatorLoginSnapshot = (String) payload.get("creatorLoginSnapshot");
        String albumName = (String) payload.get("albumName");

        AlbumMemberId albumMemberId = new AlbumMemberId(UUID.fromString(albumId), UUID.fromString(userId));
        MemberInvitation memberInvitation = new MemberInvitation(
                albumMemberId,
                null,
                creatorLoginSnapshot,
                albumName
        );
        memberInvitationService.save(memberInvitation);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/invitation/retry")
    public ResponseEntity<?> retrySendingInvitation(@RequestBody Map<String, Object> payload) {
        String albumId = (String) payload.get("albumId");
        String userId = (String) payload.get("userId");
        String creatorLoginSnapshot = (String) payload.get("creatorLoginSnapshot");
        String albumName = (String) payload.get("albumName");

        AlbumMemberId albumMemberId = new AlbumMemberId(UUID.fromString(albumId), UUID.fromString(userId));
        MemberInvitation memberInvitation = new MemberInvitation(
                albumMemberId,
                null,
                creatorLoginSnapshot,
                albumName
        );

        memberInvitationService.retrySendingInvitation(memberInvitation);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("invitation/decline")
    public ResponseEntity<?> declineInvitation(@RequestParam UUID albumId, @RequestParam UUID userId) {
        memberInvitationService.declineInvitation(albumId, userId);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("invitation/accept")
    public ResponseEntity<?> acceptInvitation(@RequestParam UUID albumId, @RequestParam UUID userId) {
        memberInvitationService.acceptInvitation(albumId, userId);
        return ResponseEntity.status(201).build();
    }
}
