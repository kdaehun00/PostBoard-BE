package com.example.demo.post.domain;

import com.example.demo.comment.domain.Comment;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Transactional
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Long likes;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Long views;

    @CreationTimestamp
    @Column(name = "create_post_date")
    private LocalDateTime createPostDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserLikeToPost> userLikeToPosts = new ArrayList<>();

    @Builder
    public Post(String title, String content, Long likes, Long views, User user) {
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.views = views;
        this.user = user;
    }

    public void updateLikes(int likes){
        this.likes += likes;
    }
}
