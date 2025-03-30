package com.example.demo.comment.dto;

import com.example.demo.comment.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListDto {
    private Long commentId;
    private Long postId;
    private String nickname;
    private String profileImg;
    private String content;
    private LocalDateTime createDate;

    public CommentListDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImage();
        this.content = comment.getContent();
        this.createDate = comment.getCreateDate();
    }
}
