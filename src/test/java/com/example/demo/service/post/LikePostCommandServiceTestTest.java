package com.example.demo.service.post;

import com.example.demo.post.domain.Post;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.repository.JPAUserLikeToPostRepository;
import com.example.demo.post.service.LikePostCommandService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.JPAUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LikePostCommandServiceTestTest {

    @InjectMocks
    private LikePostCommandService likePostCommandService;

    @Mock
    private JPAUserLikeToPostRepository jpaUserLikeToPostRepository;
    @Mock
    private JPAPostRepository jpaPostRepository;
    @Mock
    private JPAUserRepository jpaUserRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .build();

        post = Post.builder()
                .likes(1L)
                .build();

    }
    @Test
    void addLikeToPost() {
        when(jpaPostRepository.findById(1L)).thenReturn(Optional.of(post));
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(jpaUserLikeToPostRepository.existsByUserAndPost(user, post)).thenReturn(false);

        // when
        likePostCommandService.pressLikeToPost(1L, 1L);

        // then
        assertThat(post.getLikes()).isEqualTo(2L);
    }

    @Test
    void deleteLikeToPost() {
        when(jpaPostRepository.findById(1L)).thenReturn(Optional.of(post));
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(jpaUserLikeToPostRepository.existsByUserAndPost(user, post)).thenReturn(true);

        likePostCommandService.pressLikeToPost(1L, 1L);

        assertThat(post.getLikes()).isZero();
    }
}
