package com.pepino.backend.repository;

import com.pepino.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    Optional<User> findByLogin(String login);
}