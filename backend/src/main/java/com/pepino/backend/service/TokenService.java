package com.pepino.backend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

import java.time.Duration;

public interface TokenService {
    void createTokenCookies(Authentication authenticationToken, HttpServletResponse response);
}
