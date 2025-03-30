package com.example.demo.service.user;


import com.example.demo.user.service.RedisTokenService;
import org.junit.jupiter.api.Test;

import com.example.demo.user.enums.TokenTime;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisTokenServiceTest {

    @InjectMocks
    private RedisTokenService redisTokenService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Test
    void RefreshToken_저장_가져오기() {
        // given
        String email = "test@example.com";
        String refreshToken = "refreshToken123";
        long expirationMills = TokenTime.REFRESH_EXP.getExpire();
        String expectedKey = "token:" + email;

        // 저장
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        redisTokenService.saveRefreshToken(email, refreshToken, expirationMills);

        // then
        verify(valueOperations, times(1))
                .set(expectedKey, refreshToken, expirationMills, TimeUnit.MILLISECONDS);

        // 가져오기
        when(redisTemplate.opsForValue().get(expectedKey)).thenReturn(refreshToken);

        // when
        String result = redisTokenService.getRefreshToken(email);

        // then
        assertThat(result).isEqualTo(refreshToken);
    }

    @Test
    void RefreshToken_삭제() {
        // given
        String email = "test@example.com";
        String key = "token:" + email;

        // when
        redisTokenService.deleteRefreshToken(email);

        // then
        verify(redisTemplate, times(1)).delete(key);
    }
}
