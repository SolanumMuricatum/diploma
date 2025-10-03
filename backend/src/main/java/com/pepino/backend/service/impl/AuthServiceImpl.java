package com.pepino.backend.service.impl;

import com.pepino.backend.config.auth.JwtCore;
import com.pepino.backend.config.auth.UserDetailsImpl;
import com.pepino.backend.config.properties.PublicEndpointsProperties;
import com.pepino.backend.dto.LoginRequestDto;
import com.pepino.backend.exception.UserException;
import com.pepino.backend.repository.UserRepository;
import com.pepino.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PublicEndpointsProperties publicEndpointsProperties;
    private final JwtCore jwtCore;
    @Override
    public String login(
            @RequestBody LoginRequestDto loginRequestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws BadCredentialsException, UserException {
        Authentication authentication;
        if (!isUserExist(loginRequestDTO.getLogin())) throw new UserException("Пользователь с таким логином не найден");

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getLogin(),
                            loginRequestDTO.getPassword())
            );
        } catch (Exception e){
            throw new UserException("Введён неправильный пароль");
        }
        //не обязательно! контекст будет ставиться при токен фильтре
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        //response.setHeader("Authorization", "Bearer " + jwt);

        ResponseCookie cookie = ResponseCookie.from("AuthToken", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return jwt;
    }

    @Override
    public boolean isUserExist(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    @Override
    public UUID authCheck() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetailsImpl) {

            return userDetailsImpl.getId();
        }

        throw new UserException("Пользователь не найден");
    }
}
