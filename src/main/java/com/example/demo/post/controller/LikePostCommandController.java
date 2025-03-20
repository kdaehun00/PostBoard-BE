package com.example.demo.post.controller;

import com.example.demo.post.service.LikePostCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes/{userId}")
public class LikePostCommandController {
    final LikePostCommandService likePostCommandService;

    public LikePostCommandController(LikePostCommandService likePostCommandService) {
        this.likePostCommandService = likePostCommandService;
    }

    // post 좋아요 추가
    @PostMapping
    public ResponseEntity<Long> addLikeToPost(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok().body(likePostCommandService.pressLikeToPost(userId, postId));
    }

    // post 좋아요 삭제
    @DeleteMapping
    public ResponseEntity<Long> deleteLikeToPost(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok().body(likePostCommandService.pressLikeToPost(userId, postId));
    }
}
