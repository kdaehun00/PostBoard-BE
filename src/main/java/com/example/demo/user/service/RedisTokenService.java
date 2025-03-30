package com.example.demo.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_PREFIX = "token:";

   public void saveRefreshToken(String email, String refreshToken, long expirationMills) {
        redisTemplate.opsForValue().set(
                TOKEN_PREFIX + email,
                refreshToken,
                expirationMills,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(TOKEN_PREFIX + email);
    }
}
