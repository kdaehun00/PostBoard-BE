package com.example.demo.service.post;

import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.post.utils.PostSlug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class PostSlugTest {

    @InjectMocks
    PostSlug postSlug;

    @Mock
    JPAPostRepository jpaPostRepository;


    @Test
    void 특수_문자_제거() {
        // given
        String beforeTitle = "title!!";
        String afterTitle = "title";

        // then
        assertThat(postSlug.toSlug(beforeTitle)).isEqualTo(afterTitle);
    }

    @Test
    void Slug_URL_중복_있음() {
        // given
        when(jpaPostRepository.existsByUser_NicknameAndSlug(anyString(), eq("title"))).thenReturn(Boolean.TRUE);
        when(jpaPostRepository.existsByUser_NicknameAndSlug(anyString(), eq("title-1"))).thenReturn(Boolean.FALSE);

        // then
        assertThat(postSlug.validSlug("test", "title")).isEqualTo("title-1");
    }
}
