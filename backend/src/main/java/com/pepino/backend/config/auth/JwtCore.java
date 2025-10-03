package com.pepino.backend.config.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtCore {
    @Value("${app.token.secret}")
    private String secret;
    @Value("${app.token.expirationMs}")
    private int lifetime;
    private Key key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(key)
                .compact();
    }

    public String getNameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key) // проверка подписи
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
