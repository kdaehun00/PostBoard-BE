package com.example.demo.post.service;

import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.ModifyPostRequestDto;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.user.repository.JPAUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostModifyService {
    JPAPostRepository jpaPostRepository;
    JPAUserRepository jpaUserRepository;

    @Autowired
    public PostModifyService(JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Transactional
    public LikeViewResponseDto addView(Long postId) {
        Post post = getByPost(postId);
        post.setViews(post.getViews() + 1);
        return new LikeViewResponseDto(post.getViews());
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequestDto modifyPostRequestDto) {
        Post post = getByPost(postId);
        post.setTitle(modifyPostRequestDto.getTitle());
        post.setContent(modifyPostRequestDto.getContent());
        jpaPostRepository.save(post);
    }

    public Post getByPost(Long postId) {
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));
    }
}
