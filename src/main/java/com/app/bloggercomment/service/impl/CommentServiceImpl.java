package com.app.bloggercomment.service.impl;


import com.app.bloggercomment.exception.ResourceNotFoundException;
import com.app.bloggercomment.model.Comment;
import com.app.bloggercomment.model.Post;
import com.app.bloggercomment.model.User;
import com.app.bloggercomment.payload.CommentDto;
import com.app.bloggercomment.repository.CommentRepository;
import com.app.bloggercomment.service.CommentService;
import com.app.bloggercomment.service.PostProxyService;
import com.app.bloggercomment.service.UserProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentRepository commentRepository;

    @Autowired
    private PostProxyService postProxyService;
    @Autowired
    private UserProxyService userProxyService;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(Long postId, Long userId, CommentDto commentDto) {
        logger.info("creating comment calling post proxy");
        // Calling post service
        Post postEntity = postProxyService.getPost(postId);
        if (postEntity == null) throw new ResourceNotFoundException("Post", "id", postId);
        logger.info("post get success from post proxy");

        User user = userProxyService.findUser(userId.toString(), "id");
        if (user == null) throw new ResourceNotFoundException("User", "id", userId);

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
        logger.info("validating comment calling post proxy");
        Post postEntity = postProxyService.getPost(postId);
        logger.info("validating comment post found");
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
}
