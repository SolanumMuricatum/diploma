package com.pepino.userservice.service;

import com.pepino.userservice.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User saveUser(User user) throws Exception;
    List<User> searchForUser(String query, UUID albumId) throws Exception;
    void deleteUser(String password, UUID userId) throws Exception;
    Optional<User> findByLogin(String login);
    List<User> findAllByIds(List<UUID> ids);
    User updateUserLogin(String login, UUID userId) throws Exception;
    User updateUserEmail(String email, UUID userId) throws Exception;
    void updateUserPassword(String oldPassword, String newPassword, UUID userId) throws Exception;
}
