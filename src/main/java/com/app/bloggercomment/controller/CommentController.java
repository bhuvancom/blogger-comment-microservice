package com.app.bloggercomment.controller;


import com.app.bloggercomment.model.Comment;
import com.app.bloggercomment.payload.CommentDto;
import com.app.bloggercomment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestHeader(name = "userId") Long userId, @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, userId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long postId, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(postId, id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long id, @Valid @RequestBody CommentDto commentDto) {
        Comment updatedCommentDto = commentService.updateComment(postId, id, commentDto);
        return ResponseEntity.ok(updatedCommentDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Comment deleted successfully !", HttpStatus.OK);
    }
}
