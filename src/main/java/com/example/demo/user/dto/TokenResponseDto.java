package com.example.demo.user.dto;

import lombok.Data;

@Data
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String newAccessToken, String refreshToken) {
        this.accessToken = newAccessToken;
        this.refreshToken = refreshToken;
    }
}
