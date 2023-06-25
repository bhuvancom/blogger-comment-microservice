package com.app.bloggercomment.service;


import com.app.bloggercomment.payload.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
