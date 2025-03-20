package com.example.demo.user.repository;

import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
