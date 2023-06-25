package com.app.bloggercomment.service;


import com.app.bloggercomment.model.Comment;
import com.app.bloggercomment.payload.CommentDto;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, Long userId, CommentDto commentDto);

    List<Comment> getCommentsByPostId(Long postId);

    Comment getCommentById(Long postId, Long id);

    Comment updateComment(Long postId, Long id, CommentDto commentDto);

    void deleteComment(Long postId, Long id);
}
