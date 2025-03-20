package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentListDto;
import com.example.demo.comment.dto.CreateCommentRequestDto;
import com.example.demo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    // 뎃글 작성
    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable Long postId,
                                                                 @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        commentService.createComment(postId, createCommentRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment( @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    // 게시물 전체 댓글 조회
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<CommentListDto>> selectCommentList(@PathVariable Long postId){
        return commentService.selectCommentList(postId);
    }
}
