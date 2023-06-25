package com.app.bloggercomment.repository;


import com.app.bloggercomment.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
