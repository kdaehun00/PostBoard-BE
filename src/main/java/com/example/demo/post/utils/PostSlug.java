package com.example.demo.post.utils;

import com.example.demo.post.repository.JPAPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSlug {
    JPAPostRepository jpaPostRepository;

    @Autowired
    public PostSlug(JPAPostRepository jpaPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
    }

    public String toSlug(String title) {// 공백 -> 하이픈
        return title.toLowerCase()
                .replaceAll("[^a-z0-9가-힣\\s]", "") // 특수문자 제거
                .replaceAll("\\s+", "-");
    }

    public String validSlug(String nickname, String slug) {
        String baseSlug = slug;
        int index = 1;
        while (jpaPostRepository.existsByUser_NicknameAndSlug(nickname, slug)) {
            slug = baseSlug + "-" + index;
            index++;
        }
        return slug;
    }
}
