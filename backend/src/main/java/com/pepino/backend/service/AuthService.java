package com.pepino.backend.service;

import com.pepino.backend.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthService {

    String login(LoginRequestDto requestDTO, HttpServletRequest request,
                 HttpServletResponse response) throws Exception;

    boolean isUserExist(String email);
    UUID authCheck() throws Exception;
}