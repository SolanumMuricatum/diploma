package com.pepino.userservice.controller;

import com.pepino.userservice.entity.User;
import com.pepino.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        return ResponseEntity.status(201).body(userService.saveUser(user)); //return dto only!!
    }

    @GetMapping("/findByLogin/{login}")
    public ResponseEntity<?> findByLogin(@PathVariable String login) {
        Optional<User> userOptional = userService.findByLogin(login);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

}
