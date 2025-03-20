package com.example.demo.post.service;

import com.example.demo.post.domain.Post;

import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.PostResponseDto;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.repository.JPAUserLikeToPostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PostQueryService {
    JPAPostRepository jpaPostRepository;
    JPAUserRepository jpaUserRepository;
    JPAUserLikeToPostRepository jpaUserLikeToPostRepository;

    public PostQueryService(JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository,JPAUserLikeToPostRepository jpaUserLikeToPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaUserLikeToPostRepository = jpaUserLikeToPostRepository;
    }

    public List<PostResponseDto> lookUpPost() {
        List<Post> posts = jpaPostRepository.findAll();
        return posts.stream().map(PostResponseDto::new).toList();
    }


    public PostResponseDto getPostDetail(Long postId) {
        return new PostResponseDto(getByPost(postId));
    }

    // 초기 게시물 Like 수와 내가 좋아요를 눌렀는지 확인
    public LikeViewResponseDto checkLikeStatusToPost(Long postId, Long userId) {
        boolean likes = jpaUserLikeToPostRepository.existsByUserAndPost(getByUser(userId), getByPost(postId));
        long countLike = jpaUserLikeToPostRepository.countByPost(getByPost(postId));
        return new LikeViewResponseDto(likes, countLike);
    }


    public User getByUser(Long userId) {
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
    }

    @CachePut(value = "postCache", key = "#postId")
    public Post getByPost(Long postId) {
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));
    }
}
