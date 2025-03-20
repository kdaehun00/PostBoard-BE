package com.example.demo.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    private final Long userId;
    private final String content;

    public CreateCommentRequestDto(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
