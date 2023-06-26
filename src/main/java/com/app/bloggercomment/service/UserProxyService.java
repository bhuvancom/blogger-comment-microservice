package com.app.bloggercomment.service;

import com.app.bloggercomment.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "blog-user-service")
public interface UserProxyService {
    @GetMapping("/findBy/{findBy}/{value}")
    public User findUser(@PathVariable String value, @PathVariable String findBy);
}
