package com.pepino.backend.service.impl;

import com.pepino.backend.entity.User;
import com.pepino.backend.exception.UserException;
import com.pepino.backend.repository.UserRepository;
import com.pepino.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) throws UserException {
        Example<User> example = Example.of(user);
        if (userRepository.exists(example)) {
            throw new UserException("Пользователь с таким адресом электронной почты уже существует");
        } else {
            userRepository.save(user);
            System.out.printf(String.valueOf(user.getId()));
        }
    }
}
