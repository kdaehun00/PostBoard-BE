package com.example.demo.post.repository;

import com.example.demo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAPostRepository extends JpaRepository<Post, Long> {
    boolean existsByUser_NicknameAndSlug(String nickname, String slug);

    Post getPostBySlug(String slug);

    Optional<Post> findByuserIdAndSlug(long userId, String postSlug);
}
