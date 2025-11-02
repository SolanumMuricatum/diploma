package com.pepino.userservice.service;

import com.pepino.userservice.entity.User;
import java.util.Optional;

public interface UserService {
    public User saveUser(User user) throws Exception;
    public Optional<User> findByLogin(String login);
}
