package com.pepino.backend.config.auth;

import com.pepino.backend.config.properties.PublicEndpointsProperties;
import com.pepino.backend.exception.AuthHeaderException;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
@NonNullApi
public class TokenFilter extends OncePerRequestFilter {
    private final JwtCore jwtCore;
    private final PublicEndpointsProperties publicEndpointsProperties;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        //deactivate filter for a public endpoint to not throw exceptions
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            validateAndExtractToken(request);
        } catch (AuthHeaderException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }

    private void validateAndExtractToken(HttpServletRequest request) {
        /*String jwt;
        String login = null;

        String headerAuth = request.getHeader("Authorization");
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            throw new AuthHeaderException("Authorization header must start with 'Bearer'");
        }

        jwt = headerAuth.substring(7);
        if (jwt.equals("null")) {
            throw new AuthHeaderException("Bearer token is empty");
        }

        try{
            login = jwtCore.getNameFromJwt(jwt);
        } catch (ExpiredJwtException e){
            throw new AuthHeaderException("Bearer token is expired");
        }

        if(login != null && SecurityContextHolder.getContext().getAuthentication() == null){
            authenticate(login);
        } else {
            throw new AuthHeaderException("You are already authenticated");
        }*/

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AuthToken".equals(cookie.getName())) {
                    String jwt = cookie.getValue();
                    String username = jwtCore.getNameFromJwt(jwt);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Создаем Authentication
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    break;
                }
            }
        }
    }

    private void authenticate(String login) {
        UsernamePasswordAuthenticationToken authenticationToken;
        UserDetails userDetails;

        userDetails = userDetailsService.loadUserByUsername(login);
        authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        return (request.getRequestURI().equals(publicEndpointsProperties.getLogin()) || request.getRequestURI().equals(publicEndpointsProperties.getRegistration()));
    }
}

