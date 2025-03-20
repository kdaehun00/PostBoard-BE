package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Transactional
@Table(name = "Likes")

public class UserLikeToPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private User user;

    public UserLikeToPost(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public UserLikeToPost() {

    }
}
