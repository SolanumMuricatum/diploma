package com.pepino.userservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepino.userservice.entity.User;
import com.pepino.userservice.repository.UserRepository;
import com.pepino.userservice.service.LoginDataRecoveryService;
import com.pepino.userservice.service.UserService;
import com.pepino.userservice.service.feign.AlbumFeignRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlbumFeignRequestService albumFeignRequestService;
    private final ObjectMapper objectMapper;
    private final LoginDataRecoveryService loginDataRecoveryService;

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
    public List<User> searchForUser(String query, UUID albumId) throws Exception {

        List<UUID> usersIdInCurrentAlbum = objectMapper.convertValue(
                albumFeignRequestService.getAllUsersInAlbum(albumId),
                new TypeReference<List<User>>() {}
        ).stream().map(User::getId).toList();

        List<UUID> usersIdFromCurrentAlbumInvitations = albumFeignRequestService.getAllUsersIdInAlbumInvitations(albumId);

        Set<UUID> set = new LinkedHashSet<>(usersIdInCurrentAlbum);
        set.addAll(usersIdFromCurrentAlbumInvitations);
        List<UUID> unionList = new ArrayList<>(set);

        return userRepository.searchUsers(query, unionList);
    }

    @Override
    @Transactional
    public void deleteUser(String password, UUID userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Введён неверный пароль");
        }

        userRepository.delete(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        return userRepository.findAllByIdIn(ids);
    }

    @Override
    public User updateUserLogin(String login, UUID userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (userRepository.existsByLogin(login) && !user.getLogin().equals(login)) {
            throw new RuntimeException("Логин уже используется другим пользователем");
        }

        albumFeignRequestService.updateAlbumCreatorLogin(user.getId(), login);

        user.setLogin(login);
        return userRepository.save(user);
    }

    @Override
    public User updateUserEmail(String email, UUID userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (userRepository.existsByEmail(email) && !user.getEmail().equals(email)) {
            throw new IllegalArgumentException("Эта почта уже используется другим пользователем");
        }

        user.setEmail(email);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserPassword(String oldPassword, String newPassword, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Введён неверный старый пароль");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("Новый пароль не может быть таким же, как старый");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public void sendEmailForLoginDataRecovery(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Пользователь с такой почтой не найден!")
        );

        loginDataRecoveryService.sendEmail(email, user.getLogin(), passwordEncoder.encode(user.getPassword()));
    }
}
