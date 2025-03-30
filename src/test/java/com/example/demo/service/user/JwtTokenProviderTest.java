package com.example.demo.service.user;

import com.example.demo.user.service.JwtTokenProvider;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 비밀키 (최소 32자 이상이어야 함)
        String secretKey = "0123456789ABCDEFGHIJKLMNOPQRSTUVWX";
        jwtTokenProvider = new JwtTokenProvider(secretKey);
    }

    @Test
    void createAndValidateAccessToken() {
        // given
        String email = "test@example.com";

        // when: access token 생성
        String token = jwtTokenProvider.createAccessToken(email);

        // then: 토큰이 유효하고, 토큰에서 이메일을 추출할 수 있음
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.getEmailFromToken(token)).isEqualTo(email);
    }

    @Test
    void createRefreshToken_ShouldBeValid() {
        // when: refresh token 생성
        String token = jwtTokenProvider.createRefreshToken();

        // then: 토큰이 유효함
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void invalidToken_ShouldReturnFalse() {
        // given: 일부러 변조된 토큰
        String invalidToken = "invalid.token.value";

        // then: 토큰 검증 결과 false 반환
        assertThat(jwtTokenProvider.validateToken(invalidToken)).isFalse();
    }

    @Test
    void weakSecretKey_ShouldThrowWeakKeyException() {
        // 약한 비밀키 (예: 16자 미만)
        String weakKey = "shortkey";
        // then: 약한 키로 JwtTokenProvider 생성 시 예외 발생
        assertThatThrownBy(() -> new JwtTokenProvider(weakKey))
                .isInstanceOf(WeakKeyException.class);
    }
}
