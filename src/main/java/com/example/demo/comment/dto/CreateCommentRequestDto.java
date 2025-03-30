package com.example.demo.comment.dto;

import lombok.Data;

@Data
public class CreateCommentRequestDto {
    private Long userId;
    private String content;

    public CreateCommentRequestDto(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
