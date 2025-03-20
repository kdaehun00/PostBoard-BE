package com.example.demo.post.repository;

import com.example.demo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAPostRepository extends JpaRepository<Post, Long> {

}
