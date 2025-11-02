package com.pepino.authservice.service.impl;

import com.pepino.authservice.model.JwtAccess;
import com.pepino.authservice.model.JwtRefresh;
import com.pepino.authservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtAccess jwtAccess;
    private final JwtRefresh jwtRefresh;

    @Override
    public ResponseCookie createAccessTokenCookie(Authentication authentication) {
        String accessToken = jwtAccess.generateToken(authentication);

        return ResponseCookie.from(jwtAccess.getName(), accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(jwtAccess.getLifetime()))
                .sameSite("Strict")
                .build();
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(Authentication authentication) {
        String refreshToken = jwtRefresh.generateToken(authentication);

        return ResponseCookie.from(jwtRefresh.getName(), refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(jwtRefresh.getLifetime()))
                .sameSite("Strict")
                .build();
    }

    @Override
    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(jwtRefresh.getName())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(0))
                .sameSite("Strict")
                .build();
    }

    @Override
    public ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(jwtAccess.getName())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(0))
                .sameSite("Strict")
                .build();
    }
}

