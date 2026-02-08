package com.pepino.userservice.service;

import com.pepino.userservice.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public User saveUser(User user) throws Exception;
    public Optional<User> findByLogin(String login);
    public List<User> findAllByIds(List<UUID> ids);
}
