package com.pepino.backend.service.impl;

import com.pepino.backend.config.auth.UserDetailsImpl;
import com.pepino.backend.entity.User;
import com.pepino.backend.exception.UserException;
import com.pepino.backend.repository.UserRepository;
import com.pepino.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) throws UserException {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new UserException("Пользователь с таким логином уже существует");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("Пользователь с таким адресом электронной почты уже существует");
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        return UserDetailsImpl.build(user);
    }
}
