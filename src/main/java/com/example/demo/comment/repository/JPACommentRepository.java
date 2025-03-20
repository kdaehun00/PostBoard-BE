package com.example.demo.comment.repository;

import com.example.demo.comment.domain.Comment;
import com.example.demo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPACommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
