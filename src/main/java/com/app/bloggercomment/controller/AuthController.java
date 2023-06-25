package com.app.bloggercomment.controller;

import com.app.bloggercomment.exception.BlogAPIException;
import com.app.bloggercomment.model.User;
import com.app.bloggercomment.payload.LoginDto;
import com.app.bloggercomment.repository.UserRepository;
import com.app.bloggercomment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // Log in api
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<User> home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        Optional<User> byUsername = userRepository.findByUsername(name);
        return byUsername.map(ResponseEntity::ok).orElseThrow();
    }
}
