package com.example.demo.post.controller;

import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.ModifyPostRequestDto;
import com.example.demo.post.service.PostModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostModifyController {
    private final PostModifyService postModifyService;

    @Autowired
    public PostModifyController(PostModifyService postModifyService) {
        this.postModifyService = postModifyService;
    }

    // post 조회수 증가
    @PatchMapping("/posts/{postId}/views")
    public ResponseEntity<LikeViewResponseDto> addViewToPost(@PathVariable Long postId) {
        LikeViewResponseDto likeviewResponseDto = postModifyService.addView(postId);
        return ResponseEntity.ok().body(likeviewResponseDto);
    }

    // post 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> modifyPost(@PathVariable Long postId,
                                                            @RequestBody ModifyPostRequestDto modifyPostRequestDto) {
        postModifyService.modifyPost(postId, modifyPostRequestDto);
        return ResponseEntity.noContent().build();
    }
}
