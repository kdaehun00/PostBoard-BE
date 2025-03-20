package com.example.demo.post.repository;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.UserLikeToPost;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JPAUserLikeToPostRepository extends JpaRepository<UserLikeToPost, Long> {
    // 특정 사용자가 특정 게시물에 좋아요를 눌렀는지 확인
    boolean existsByUserAndPost(User user, Post post); // 사용자가 특정 게시물에 좋아요를 눌렀는지 확인

    // 특정 게시물의 좋아요 개수 조회
    long countByPost(Post post);

    // 특정 사용자가 특정 게시물의 좋아요를 취소 (DELETE)
    void deleteByUserAndPost(User user, Post post);
}
