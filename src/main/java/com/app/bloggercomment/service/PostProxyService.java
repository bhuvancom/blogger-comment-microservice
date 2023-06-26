package com.app.bloggercomment.service;

import com.app.bloggercomment.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// TODO put post service name here
@FeignClient(name = "blog-mainapp-service")
public interface PostProxyService {
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable(name = "id") Long id);
}
