package com.example.demo.comment.domain;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Comments")
@Transactional
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment")
    private String content;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
