package com.example.demo.post.dto;

import lombok.Data;

@Data
public class LikeViewResponseDto {
    private Long views;
    private boolean likes;
    private Long likeNumber;

    public LikeViewResponseDto(Long views) {
        this.views = views;
    }


    public LikeViewResponseDto(boolean likes, long likeNumber) {
        this.likes = likes;
        this.likeNumber = likeNumber;
    }
}
