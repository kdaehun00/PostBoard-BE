package com.example.demo.post.dto;

import com.example.demo.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private Long likes;
    private Long views;
    private String nickname;
    private String profileImg;
    private LocalDateTime createPostDate;


    public PostResponseDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;

    }

    public PostResponseDto (Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likes = post.getLikes();
        this.views = post.getViews();
        this.nickname = post.getUser().getNickname();
        this.profileImg = post.getUser().getProfileImage();
        this.createPostDate = post.getCreatePostDate();
    }
}
