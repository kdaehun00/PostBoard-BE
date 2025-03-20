package com.example.demo.post.controller;

import com.example.demo.post.dto.*;
import com.example.demo.post.service.PostCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class PostCommandController {
    private final PostCommandService postCommandService;

    public PostCommandController(PostCommandService postCommandService) {
        this.postCommandService = postCommandService;
    }

    // post 생성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        Long postId = postCommandService.createPost(createPostRequestDto);
        return ResponseEntity.created(URI.create("/posts/"+postId)).body(new PostResponseDto(postId, createPostRequestDto.getUserId()));
    }

    // post 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId) {
        postCommandService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
