package com.example.demo.post.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePostRequestDto {

    private Long userId;
    private String title;
    private String content;
}
