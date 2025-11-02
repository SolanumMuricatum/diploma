package com.pepino.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDto {
    private UUID id;
    private String email;
    private String password;
    private String login;
}

