package com.pepino.backend.service.impl;

import com.pepino.backend.config.auth.JwtAccess;
import com.pepino.backend.config.auth.JwtRefresh;
import com.pepino.backend.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
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
    public void createTokenCookies(Authentication authenticationToken, HttpServletResponse response){

        String accessToken = jwtAccess.generateToken(authenticationToken);
        String refreshToken = jwtRefresh.generateToken(authenticationToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from(jwtAccess.getName(), accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(jwtRefresh.getLifetime()))
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(jwtRefresh.getName(), refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(jwtRefresh.getLifetime()))
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
