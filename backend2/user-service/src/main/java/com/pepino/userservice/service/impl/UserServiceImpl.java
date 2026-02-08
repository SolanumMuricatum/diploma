package com.pepino.userservice.service.impl;

import com.pepino.userservice.entity.User;
import com.pepino.userservice.repository.UserRepository;
import com.pepino.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) throws Exception {
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
        if (userRepository.existsByLogin(user.getLogin()) || userRepository.existsByEmail(user.getEmail())) {
            throw new BadCredentialsException("Пользователь с таким логином или почтой уже существует");
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        return userRepository.findAllByIdIn(ids);
    }
}
