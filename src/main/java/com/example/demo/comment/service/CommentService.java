package com.example.demo.comment.service;

import com.example.demo.comment.domain.Comment;
import com.example.demo.comment.dto.CommentListDto;
import com.example.demo.comment.dto.CreateCommentRequestDto;
import com.example.demo.comment.repository.JPACommentRepository;
import com.example.demo.post.domain.Post;
import com.example.demo.post.repository.JPAPostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.post.utils.GetPost;
import com.example.demo.user.utils.GetUser;
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
    GetPost getPost;
    GetUser getUser;

    @Autowired
    public CommentService(JPACommentRepository jpaCommentRepository, JPAUserRepository jpaUserRepository,
                          JPAPostRepository postRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPostRepository = postRepository;
        this.getUser = new GetUser(jpaUserRepository);
        this.getPost = new GetPost(jpaPostRepository);
    }

    public void createComment(Long postId,
                              CreateCommentRequestDto createCommentRequestDto) {
        User user = getUser.getUserById(createCommentRequestDto.getUserId());
        Post post = getPost.getPostById(postId);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(createCommentRequestDto.getContent());
        jpaCommentRepository.save(comment);
    }

    public ResponseEntity<List<CommentListDto>> selectCommentList(Long postId) {
        return ResponseEntity.ok().body(
                jpaCommentRepository.findByPost(getPost.getPostById(postId)).stream()
                .map(CommentListDto::new)
                .collect(Collectors.toList())
        );
    }

    public void deleteComment(Long postId, Long commentId) {
        getPost.getPostById(postId);
        jpaCommentRepository.deleteById(commentId);
    }
}
