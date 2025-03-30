package com.example.demo.post.service;

import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.ModifyPostRequestDto;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.post.utils.GetPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostModifyService {
    JPAPostRepository jpaPostRepository;
    JPAUserRepository jpaUserRepository;
    GetPost getPost;

    @Autowired
    public PostModifyService(JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.getPost = new GetPost(jpaPostRepository);
    }

    @Transactional
    public LikeViewResponseDto addView(Long postId) {
        Post post = getPost.getPostById(postId);
        post.setViews(post.getViews() + 1);
        return new LikeViewResponseDto(post.getViews());
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequestDto modifyPostRequestDto) {
        Post post = getPost.getPostById(postId);
        post.setTitle(modifyPostRequestDto.getTitle());
        post.setContent(modifyPostRequestDto.getContent());
        jpaPostRepository.save(post);
    }
}
