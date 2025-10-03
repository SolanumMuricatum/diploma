package com.pepino.backend.controller;

import com.pepino.backend.dto.LoginRequestDto;
import com.pepino.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto loginRequestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        return ResponseEntity.ok(authService.login(loginRequestDTO, request, response));
    }

    @GetMapping("/auth/check")
    public ResponseEntity<?> authCheck() throws Exception {
        return ResponseEntity.ok(authService.authCheck());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.ok("The exit was successful");
    }
}