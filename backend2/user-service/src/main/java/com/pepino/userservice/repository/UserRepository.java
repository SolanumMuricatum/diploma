package com.pepino.userservice.repository;

import com.pepino.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    Optional<User> findByLogin(String login);
    List<User> findAllByIdIn(List<UUID> ids);

    @Query("""
                SELECT u FROM User u
                WHERE LOWER(u.login) LIKE LOWER(CONCAT(:query, '%'))
                AND (:excludedIds IS NULL OR u.id NOT IN :excludedIds)
            """)
    List<User> searchUsers(@Param("query") String query, @Param("excludedIds") List<UUID> excludedIds);
}
