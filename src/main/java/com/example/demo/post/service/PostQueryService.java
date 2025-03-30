package com.example.demo.post.service;

import com.example.demo.post.domain.Post;

import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.PostResponseDto;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.repository.JPAUserLikeToPostRepository;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.post.utils.GetPost;
import com.example.demo.user.utils.GetUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PostQueryService {
    JPAPostRepository jpaPostRepository;
    JPAUserRepository jpaUserRepository;
    JPAUserLikeToPostRepository jpaUserLikeToPostRepository;
    GetPost getPost;
    GetUser getUser;

    public PostQueryService(JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository,JPAUserLikeToPostRepository jpaUserLikeToPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaUserLikeToPostRepository = jpaUserLikeToPostRepository;
        this.getPost = new GetPost(jpaPostRepository);
        this.getUser = new GetUser(jpaUserRepository);
    }

    public List<PostResponseDto> lookUpPost() {
        List<Post> posts = jpaPostRepository.findAll();
        return posts.stream().map(PostResponseDto::new).toList();
    }


    public PostResponseDto getPostDetail(String userName, String postSlug) {
        Post post = jpaPostRepository.findByUser_NicknameAndSlug(userName, postSlug)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));
        return new PostResponseDto(post);
    }

    // 초기 게시물 Like 수와 내가 좋아요를 눌렀는지 확인
    public LikeViewResponseDto checkLikeStatusToPost(Long postId, Long userId) {
        boolean likes = jpaUserLikeToPostRepository.existsByUserAndPost(getUser.getUserById(userId), getPost.getPostById(postId));
        long countLike = jpaUserLikeToPostRepository.countByPost(getPost.getPostById(postId));
        return new LikeViewResponseDto(likes, countLike);
    }

}
