package com.example.demo.post.controller;

import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.PostResponseDto;
import com.example.demo.post.service.PostQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostQueryController {

    private final PostQueryService postQueryService;

    public PostQueryController(PostQueryService postQueryService) {
        this.postQueryService = postQueryService;
    }

    // post 목록 찾기
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> findAllPost() {
        return ResponseEntity.ok().body(postQueryService.lookUpPost());
    }

    // post 상세 보기
    @GetMapping("/@{userName}/{postSlug}")
    public ResponseEntity<PostResponseDto> getPostDetail(@PathVariable String userName, @PathVariable String postSlug) {
        return ResponseEntity.ok().body(postQueryService.getPostDetail(userName, postSlug));
    }

    // post 좋아요 초기
    @GetMapping("/posts/{postId}/likes/{userId}")
    public LikeViewResponseDto checkLikeStatusToPost(@PathVariable Long postId, @PathVariable Long userId) {
        return postQueryService.checkLikeStatusToPost(postId, userId);
    }
}
