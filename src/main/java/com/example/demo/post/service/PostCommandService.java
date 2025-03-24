package com.example.demo.post.service;

import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostCommandService {
    JPAPostRepository jpaPostRepository;
    JPAUserRepository jpaUserRepository;

    public PostCommandService(JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @CachePut(value = "postCache", key = "#result")
    @Transactional
    public Long createPost(CreatePostRequestDto createPostRequestDto) {
        User user = jpaUserRepository.findById(createPostRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));

        Post post = Post.builder()
                .title(createPostRequestDto.getTitle())
                .slug(createPostRequestDto.getSlug())
                .content(createPostRequestDto.getContent())
                .likes(0L)
                .views(0L)
                .user(user)
                .build();
        jpaPostRepository.save(post);
        return post.getPostId();
    }

    public String toSlug(String title) {// 공백 -> 하이픈
        return title.toLowerCase()
                .replaceAll("[^a-z0-9가-힣\\s]", "") // 특수문자 제거
                .replaceAll("\\s+", "-");
    }

    public String validSlug(String nickname, String slug) {
        String baseSlug = slug;
        int index = 1;
        while (jpaPostRepository.existsByUser_NicknameAndSlug(nickname, slug)) {
            slug = baseSlug + "-" + index;
            index++;
        }
        return slug;
    }

    @CacheEvict(value = "postCache", key = "#postId")
    @Transactional
    public void deletePost(Long postId) {
        if (!jpaPostRepository.existsById(postId)) {
            throw new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage());
        }
        jpaPostRepository.deleteById(postId);
    }
}
