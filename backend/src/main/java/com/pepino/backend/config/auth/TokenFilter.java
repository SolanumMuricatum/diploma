package com.pepino.backend.config.auth;

import com.pepino.backend.config.properties.PublicEndpointsProperties;
import com.pepino.backend.exception.AuthException;
import com.pepino.backend.service.AuthService;
import com.pepino.backend.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@NonNullApi
public class TokenFilter extends OncePerRequestFilter {
    private final JwtAccess jwtAccess;
    private final JwtRefresh jwtRefresh;
    private final PublicEndpointsProperties publicEndpointsProperties;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    private String userNameFromRefreshToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //deactivate filter for a public endpoint to not throw exceptions
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            extractToken(request, response);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        return (request.getRequestURI().equals(publicEndpointsProperties.getLogin()) || request.getRequestURI().equals(publicEndpointsProperties.getRegistration()));
    }

    private void extractToken(HttpServletRequest request, HttpServletResponse response) throws AuthException {
        Cookie[] cookies = request.getCookies();
        List<Cookie> tokenCookies = Arrays.stream(cookies)
                .filter(cookie -> (cookie.getName().equals(jwtAccess.getName()) || cookie.getName().equals(jwtRefresh.getName())))
                .toList();
        if (!tokenCookies.isEmpty() && tokenCookies.size() != 1) { //чекаем пустой ли лист или там только 1 токен
            for (Cookie cookie : tokenCookies) {
                validateToken(cookie, response);
            }
        } else {
            throw new AuthException("No cookies found");
        }
        //TODO проверка на то есть ли кука с токеном доступа вообще
    }

    private void validateToken(Cookie cookie, HttpServletResponse response) throws AuthException {
        if (jwtAccess.getName().equals(cookie.getName())) {
            processAccessToken(cookie, response);
        } else if (jwtRefresh.getName().equals(cookie.getName())) {
            userNameFromRefreshToken = processRefreshToken(cookie) ;
        }
    }

    private void processAccessToken(Cookie cookie, HttpServletResponse response) throws AuthException {
        String jwt = cookie.getValue();
        String username;
        boolean isTokenExpiredFlag = false;

        try{

            username = jwtAccess.getUsernameFromJwt(jwt);
            if(username == null) {
                throw new AuthException("Invalid access token, please login");
            }
            authenticate(username, isTokenExpiredFlag, response);

        } catch (ExpiredJwtException e) {

            isTokenExpiredFlag = true;
            String expiredUsername = e.getClaims().getSubject(); //достаём username прямо из exception
            if (expiredUsername == null) {
                throw new AuthException("Invalid access token, please login");
            }

            if(userNameFromRefreshToken.equals(expiredUsername)) {
                authenticate(expiredUsername, isTokenExpiredFlag, response);
            }

        } catch (JwtException e) {
            throw new AuthException("Invalid access token, please login");
        }
    }

    private void authenticate(String username, boolean IsTokenExpiredFlag, HttpServletResponse response) throws AuthException {
        if(SecurityContextHolder.getContext().getAuthentication() == null) { //на всякий
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Создаем Authentication если всё гуд
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            if (IsTokenExpiredFlag) {
                tokenService.createTokenCookies(authToken, response);
            }
        }
    }

    private String processRefreshToken(Cookie cookie){
        String jwt = cookie.getValue();
        String username;
        try{
            username = jwtRefresh.getUsernameFromJwt(jwt);
            if(username == null) {
                throw new AuthException("Invalid refresh token, please login");
            }
            return username;
        } catch (ExpiredJwtException e) {
            throw new AuthException("Expired refresh token, please login");
        } catch (JwtException e){
            throw new AuthException("Invalid refresh token, please login");
        }
    }

/*    private String getUsernameFromJwt(String jwt, String name) {
        try{
            return jwtAccess.getUsernameFromJwt(jwt);
        } catch (ExpiredJwtException e) {
            if(processIfAccessTokenIsExpired() || name.equals("AccessToken")) {
                throw new AuthException("Expired JWT access and refresh token, please login");
            }
        }
    }*/



/*    private void authenticate(String login) {
        UsernamePasswordAuthenticationToken authenticationToken;
        UserDetails userDetails;

        userDetails = userDetailsService.loadUserByUsername(login);
        authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }*/

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
}

