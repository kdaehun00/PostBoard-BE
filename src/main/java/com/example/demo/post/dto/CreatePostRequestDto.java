package com.example.demo.post.dto;


import lombok.Data;

@Data
public class CreatePostRequestDto {

    private Long userId;
    private String userNickname;
    private String title;
    private String slug;
    private String content;
}
