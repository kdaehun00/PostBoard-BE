package com.example.demo.post.enums;

import lombok.Getter;

@Getter
public enum PostMessage {
    CREATE_POST_SUCCESS("게시물 작성 완료"),
    NON_EXIST_POST("게시물 없음.");

    private final String message;

    PostMessage(String message) {
        this.message = message;
    }
}
