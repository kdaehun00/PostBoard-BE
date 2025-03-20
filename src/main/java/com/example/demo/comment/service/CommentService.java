package com.example.demo.comment.service;

import com.example.demo.comment.domain.Comment;
import com.example.demo.comment.dto.CommentListDto;
import com.example.demo.comment.dto.CreateCommentRequestDto;
import com.example.demo.comment.repository.JPACommentRepository;
import com.example.demo.post.domain.Post;
import com.example.demo.post.enums.PostMessage;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentService {
    JPACommentRepository jpaCommentRepository;
    JPAUserRepository jpaUserRepository;
    JPAPostRepository jpaPostRepository;

    @Autowired
    public CommentService(JPACommentRepository jpaCommentRepository, JPAUserRepository jpaUserRepository,
                          JPAPostRepository postRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPostRepository = postRepository;
    }

    public void createComment(Long postId,
                              CreateCommentRequestDto createCommentRequestDto) {
        User user = getUser(createCommentRequestDto.getUserId());
        Post post = getPost(postId);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(createCommentRequestDto.getContent());
        jpaCommentRepository.save(comment);
    }

    public ResponseEntity<List<CommentListDto>> selectCommentList(Long postId) {
        return ResponseEntity.ok().body(
                jpaCommentRepository.findByPost(getPost(postId)).stream()
                .map(CommentListDto::new)
                .collect(Collectors.toList())
        );
    }

    public void deleteComment(Long postId, Long commentId) {
        getPost(postId);
        jpaCommentRepository.deleteById(commentId);
    }


    public User getUser(Long userId) {
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
    }

    public Post getPost(Long postId) {
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(PostMessage.NON_EXIST_POST.getMessage()));
    }
}
