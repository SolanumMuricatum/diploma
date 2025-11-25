package com.pepino.albumservice.config;

import com.pepino.albumservice.service.AuthFeignRequestService;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@NonNullApi
public class TokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    private final AuthFeignRequestService authFeignRequestService;
    private static final AtomicInteger requestCounter = new AtomicInteger(0);
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = validateAndExtractToken(request);
            authenticate(token);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String validateAndExtractToken(HttpServletRequest request) throws Exception {
        String headerAuth = request.getHeader("Authorization");
        /*System.out.println("Authorization header: " + headerAuth);

        int id = requestCounter.incrementAndGet();
        String url = request.getRequestURI();
        String method = request.getMethod();
        Thread thread = Thread.currentThread();

        System.out.println("[" + id + "] Thread: " + thread.getName() +
                " | Method: " + method +
                " | URL: " + url +
                " | Has token: " + (headerAuth != null && headerAuth.startsWith("Bearer")));

*/
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            throw new Exception("Authorization header must start with 'Bearer'");
        }

        String token = headerAuth.substring(7);
        if (token.isEmpty()) {
            throw new Exception("Bearer token is empty");
        }
        //TODO тут проверить сработает ли изЭмпти или оставить .equals("null") хоть бредово

        return token;
    }

    private void authenticate(String token) throws Exception {

        var kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(authFeignRequestService.getInternalServicePublicKey())));

        try {
            String serviceName = Jwts.parser()
                    .verifyWith(publicKey) // проверка подписи
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            if (serviceName == null) {
                throw new Exception("Service name is null");
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    serviceName,
                    null,
                    null
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            logger.error("Failed to authenticate service: {}", e.getMessage());
            throw new Exception("Failed to authenticate service");
        }
    }
}
