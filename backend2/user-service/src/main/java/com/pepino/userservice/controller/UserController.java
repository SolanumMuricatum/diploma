package com.pepino.userservice.controller;

import com.pepino.userservice.entity.User;
import com.pepino.userservice.service.PhotoService;
import com.pepino.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PhotoService photoService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        return ResponseEntity.status(201).body(userService.saveUser(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Object> payload) throws Exception {
        String password = (String) payload.get("password");
        String userId = (String) payload.get("userId");
        userService.deleteUser(password, UUID.fromString(userId));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/findByLogin/{login}")
    public ResponseEntity<?> findByLogin(@PathVariable String login) {
        Optional<User> userOptional = userService.findByLogin(login);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/account/save/photo")
    public ResponseEntity<?> saveUserAccountPhoto(@RequestBody Map<String, Object> payload) throws Exception {
        String photoBase64 = (String) payload.get("image");
        String userId = (String) payload.get("userId");
        return ResponseEntity.status(200).body(photoService.saveAccountPhoto(photoBase64, UUID.fromString(userId)));
    }

    @DeleteMapping("/account/delete/photo")
    public ResponseEntity<?> deleteAccountPhoto(@RequestParam UUID userId) throws Exception {
        photoService.deleteAccountPhoto(userId);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/account/login/update")
    public ResponseEntity<?> updateUserLogin(@RequestBody Map<String, Object> payload) throws Exception {
        String login = (String) payload.get("login");
        String userId = (String) payload.get("userId");
        return ResponseEntity.status(200).body(userService.updateUserLogin(login, UUID.fromString(userId)));
    }

    @PutMapping("/account/email/update")
    public ResponseEntity<?> updateUserEmail(@RequestBody Map<String, Object> payload) throws Exception {
        String email = (String) payload.get("email");
        String userId = (String) payload.get("userId");
        return ResponseEntity.status(200).body(userService.updateUserEmail(email, UUID.fromString(userId)));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> findAllByIds(@RequestParam List<UUID> ids) {
        return ResponseEntity.status(200).body(userService.findAllByIds(ids));
    }

    @PutMapping("/account/password/update")
    public ResponseEntity<?> updateUserPassword(@RequestBody Map<String, Object> payload) throws Exception {
        String oldPassword = (String) payload.get("oldPassword");
        String newPassword = (String) payload.get("newPassword");
        String userId = (String) payload.get("userId");
        userService.updateUserPassword(oldPassword, newPassword, UUID.fromString(userId));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchForUser(@RequestParam String query, @RequestParam UUID albumId) throws Exception {
        return ResponseEntity.status(200).body(userService.searchForUser(query, albumId));
    }
}
