package com.example.demo.post.utils;

import com.example.demo.post.domain.Post;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
public class GetPost {
    JPAPostRepository jpaPostRepository;

    @Autowired
    public GetPost(JPAPostRepository jpaPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
    }

    @CachePut(value = "postCache", key = "#postId")
    public Post getPostById(long postId){
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));
    }
}
