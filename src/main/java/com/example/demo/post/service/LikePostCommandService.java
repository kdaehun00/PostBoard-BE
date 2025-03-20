package com.example.demo.post.service;

import com.example.demo.aop.ExecutionTime;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.UserLikeToPost;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.repository.JPAUserLikeToPostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LikePostCommandService {
    JPAPostRepository jpaPostRepository;
    JPAUserLikeToPostRepository jpaUserLikeToPostRepository;
    JPAUserRepository jpaUserRepository;

    public LikePostCommandService(JPAUserLikeToPostRepository jpaUserLikeToPostRepository, JPAPostRepository jpaPostRepository, JPAUserRepository jpaUserRepository) {
        this.jpaUserLikeToPostRepository = jpaUserLikeToPostRepository;
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @ExecutionTime
    @Transactional
    public Long pressLikeToPost(Long userId, Long postId) {
        Post post = jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));

        User user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));

        boolean addOrRemove = jpaUserLikeToPostRepository.existsByUserAndPost(user, post);

        if (!addOrRemove) {
            jpaUserLikeToPostRepository.save(new UserLikeToPost(user, post));
            post.setLikes(post.getLikes() + 1);
        }else {
            jpaUserLikeToPostRepository.deleteByUserAndPost(user, post);
            post.setLikes(post.getLikes() - 1);
        }
        return jpaUserLikeToPostRepository.countByPost(post);
    }
}
