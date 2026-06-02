package com.pepino.authservice.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
