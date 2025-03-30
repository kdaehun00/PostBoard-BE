package com.example.demo.service.post;

import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.service.PostCommandService;
import com.example.demo.post.utils.PostSlug;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.JPAUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

    @InjectMocks
    PostCommandService postCommandService;

    @Mock
    JPAPostRepository jpaPostRepository;

    @Mock
    JPAUserRepository jpaUserRepository;

    @Mock
    PostSlug postSlug;


    @Test
    void 게시물_생성() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("test")
                .build();

        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto();
        createPostRequestDto.setTitle("title!!");
        createPostRequestDto.setUserId(1L);
        createPostRequestDto.setUserNickname(user.getNickname());

        when(jpaUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postSlug.toSlug(anyString())).thenReturn("title");
        when(postSlug.validSlug(anyString(), anyString())).thenReturn("title-1");


        when(jpaPostRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            ReflectionTestUtils.setField(post, "postId", 1L);
            return post;
        });

        // when
        Long postId = postCommandService.createPost(createPostRequestDto);

        // then
        assertThat(postId).isEqualTo(1L);
    }

    @Test
    void 게시물_삭제_실패() {
        // when
        when(jpaPostRepository.existsById(anyLong())).thenReturn(false);

        // then
        assertThatThrownBy(() -> postCommandService.deletePost(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PostMessage.NON_EXIST_POST.getMessage());
    }

    @Test
    void 게시물_삭제() {
        // given
        when(jpaPostRepository.existsById(anyLong())).thenReturn(true);

        // when
        postCommandService.deletePost(1L);

        // then: jpaPostRepository.deleteById() 메서드가 1L 인자로 호출되었는지 검증
        verify(jpaPostRepository, times(1)).deleteById(1L);
    }
}
