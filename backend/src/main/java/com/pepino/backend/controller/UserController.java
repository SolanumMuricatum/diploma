package com.pepino.backend.controller;

import com.pepino.backend.entity.User;
import com.pepino.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
        userService.saveUser(user);
        return ResponseEntity.status(201).body(user); //return dto only!!
    }
}
