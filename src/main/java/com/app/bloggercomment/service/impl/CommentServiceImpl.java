package com.app.bloggercomment.service.impl;


import com.app.bloggercomment.exception.BlogAPIException;
import com.app.bloggercomment.exception.ResourceNotFoundException;
import com.app.bloggercomment.model.Comment;
import com.app.bloggercomment.model.Post;
import com.app.bloggercomment.model.User;
import com.app.bloggercomment.payload.CommentDto;
import com.app.bloggercomment.repository.CommentRepository;
import com.app.bloggercomment.repository.PostRepository;
import com.app.bloggercomment.repository.UserRepository;
import com.app.bloggercomment.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Value("${app.blogger.main.url}")
    private String postServiceUrl;
    private CommentRepository commentRepository;
    @Autowired
    private RestTemplate restTemplate;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Comment createComment(Long postId, Long userId, CommentDto commentDto) {
        // Calling post service
        Post postEntity = restTemplate.getForEntity(postServiceUrl + "/posts/" + postId, Post.class).getBody();
        if (postEntity == null) throw new ResourceNotFoundException("Post", "id", postId);

        // TODO call auth service here for user
        User user = userRepository.findById(userId).orElseThrow(() -> new BlogAPIException(HttpStatus.NOT_FOUND, "User id not found"));

        Comment comment = toEntity(commentDto);
        Post p = new Post();
        p.setId(postId);
        comment.setPost(p);
        comment.setUser(user);
        comment.setCreatedAt(new Date());
        return commentRepository.save(comment);
    }


    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment getCommentById(Long postId, Long id) {
        return validateComment(postId, id);
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentDto commentDto) {
        Comment comment = validateComment(postId, id);
        comment.setBody(commentDto.getBody());
        return (commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long postId, Long id) {
        Comment comment = validateComment(postId, id);
        commentRepository.delete(comment);
    }

    private Comment validateComment(Long postId, Long id) {
        Post postEntity = restTemplate.getForEntity(postServiceUrl + "/posts/" + postId, Post.class).getBody();
        if (postEntity == null) throw new ResourceNotFoundException("Post", "id", postId);
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }

    private Comment toEntity(CommentDto commentDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getDetails();
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setBody(commentDto.getBody());
        comment.setCreatedAt(new Date());
        return comment;
    }

    private CommentDto toDto(Comment comment) {
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return modelMapper.map(comment, CommentDto.class);
    }
}
