package com.example.demo.user.controller;

// Class
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.service.UserAuthService;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;

// Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
public class UserAuthController {
    UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserApiResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok().body(userAuthService.join(userLoginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserApiResponseDto> signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        Long userId = userAuthService.registerUser(userSignupRequestDto);
        return ResponseEntity.created(URI.create("/signup/" + userId)).body(new UserApiResponseDto(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<UserApiResponseDto> deleteUser(@PathVariable Long userId) {
        userAuthService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}