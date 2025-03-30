package com.example.demo.user.domain;

import com.example.demo.comment.domain.Comment;
import com.example.demo.post.domain.UserLikeToPost;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 1:N 관계 테이블
import com.example.demo.post.domain.Post;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 보호
@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "password")
    private String password;

    @Setter
    @Column(name = "nickname")
    private String nickname;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createUserDate;

    @Setter
    @Column(name = "password_update_date")
    private LocalDateTime passwordUpdateDate;

    @Setter
    @Column(name = "status")
    private String status;

    @Setter
    @Column(name = "delete_date")
    private Timestamp deleteUserDate;

    @Setter
    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<UserLikeToPost> userLikeToPosts = new ArrayList<>();


    @Builder
    public User(String email, String password, String nickname, String status, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.profileImage = profileImage;
        this.createUserDate = LocalDateTime.now();
        this.passwordUpdateDate = LocalDateTime.now();
        this.deleteUserDate = null;
    }
}
