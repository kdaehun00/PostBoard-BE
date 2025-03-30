package com.example.demo.user.controller;

import com.example.demo.user.dto.TokenResponseDto;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserApiResponseDto> login(@RequestBody UserLoginRequestDto request) {
        UserApiResponseDto response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestHeader("Authorization") String refreshHeader) {
        TokenResponseDto tokenResponse = authService.refreshToken(refreshHeader);
        return ResponseEntity.ok(tokenResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String tokenHeader) {
        authService.logout(tokenHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequestDto request) {
        Long userId = authService.registerUser(request);
        return ResponseEntity.created(URI.create("/signup/" + userId)).build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        authService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
