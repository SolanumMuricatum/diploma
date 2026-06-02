package com.pepino.userservice.service;

import com.pepino.userservice.entity.User;

import java.util.UUID;

public interface PhotoService {
     User saveAccountPhoto(String photoBase64, UUID userId) throws Exception;
     void deleteAccountPhoto(UUID userId) throws Exception;
}
