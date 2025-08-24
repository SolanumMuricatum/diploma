package com.pepino.backend.controller;

import com.pepino.backend.entity.User;
import com.pepino.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBook(@RequestBody User user) throws Exception {
        System.out.println(user.getName());
        userService.saveUser(user);
        return ResponseEntity.status(201).body(user);
    }
}
