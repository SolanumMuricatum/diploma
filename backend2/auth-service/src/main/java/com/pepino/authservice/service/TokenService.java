package com.pepino.authservice.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface TokenService {
    ResponseCookie createAccessTokenCookie(Authentication authentication);
    ResponseCookie createRefreshTokenCookie(Authentication authentication);
    ResponseCookie deleteAccessTokenCookie();
    ResponseCookie deleteRefreshTokenCookie();
}
