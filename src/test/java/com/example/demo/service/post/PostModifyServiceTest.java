package com.example.demo.service.post;

import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.LikeViewResponseDto;
import com.example.demo.post.dto.ModifyPostRequestDto;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.service.PostModifyService;

import lombok.extern.slf4j.Slf4j;

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
class PostModifyServiceTest {

    @InjectMocks
    PostModifyService postModifyService;

    @Mock
    private JPAPostRepository jpaPostRepository;

    @Test
    void addView() {
        Post post = Post.builder()
                .views(1L)
                .build();

        when(jpaPostRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        postModifyService.addView(1L);
        LikeViewResponseDto response = new LikeViewResponseDto(post.getViews());

        // then
        assertThat(response.getViews()).isEqualTo(2L);
    }

    @Test
    void modifyPost() {
        // given
        Post post = Post.builder()
                .title("oldTitle")
                .content("oldContent")
                .build();

        when(jpaPostRepository.findById(1L)).thenReturn(Optional.of(post));
        when(jpaPostRepository.save(post)).thenReturn(post);

        ModifyPostRequestDto request = new ModifyPostRequestDto();
        request.setTitle("newTitle");
        request.setContent("newContent");

        // when
        postModifyService.modifyPost(1L, request);

        // then
        assertThat(post.getTitle()).isEqualTo("newTitle");
        assertThat(post.getContent()).isEqualTo("newContent");
    }
}
