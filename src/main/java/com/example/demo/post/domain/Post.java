package com.example.demo.post.domain;

import com.example.demo.comment.domain.Comment;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    @Setter
    @Column(name = "title")
    private String title;

    @Lob
    @Setter
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Setter
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Long likes;

    @Setter
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Long views;

    @Column(name = "slug")
    private String slug;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createPostDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<UserLikeToPost> userLikeToPosts = new ArrayList<>();

    @Builder
    public Post(String title, String slug, String content, Long likes, Long views, User user) {
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.likes = likes;
        this.views = views;
        this.user = user;
        this.createPostDate = LocalDateTime.now();
    }
}
