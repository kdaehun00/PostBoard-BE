package com.example.demo.user.service;

import com.example.demo.user.domain.User;
import com.example.demo.user.dto.TokenResponseDto;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.enums.TokenTime;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.utils.GetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JPAUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final GetUser getUser;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenService redisTokenService;

    // 회원가입
    @CachePut(value = "userCache", key = "#result")
    @Transactional
    public Long registerUser(UserSignupRequestDto request) {
        if (jpaUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(UserIdPwMessage.ALREADY_USE_EMAIL.getMessage());
        }
        if (jpaUserRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .status(UserStatus.ACTIVE.getStatus())
                .profileImage(request.getProfileImg())
                .build();
        jpaUserRepository.save(user);
        return user.getId();
    }

    // 로그인 및 토큰 발급
    @Transactional
    public UserApiResponseDto login(UserLoginRequestDto request) {
        User user = getUser.getUserByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(UserIdPwMessage.WRONG_PASSWORD.getMessage());
        }
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        redisTokenService.saveRefreshToken(user.getEmail(), refreshToken, TokenTime.REFRESH_EXP.getExpire());
        return new UserApiResponseDto(user, accessToken, refreshToken);
    }

    // 토큰 리프레시
    public TokenResponseDto refreshToken(String refreshHeader) {
        String refreshToken = extractToken(refreshHeader);
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String savedToken = redisTokenService.getRefreshToken(user.getEmail());
        if (!refreshToken.equals(savedToken)) {
            throw new RuntimeException("Refresh token mismatch");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        return new TokenResponseDto(newAccessToken, refreshToken);
    }

    // 로그아웃 처리
    public void logout(String tokenHeader) {
        String token = extractToken(tokenHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid access token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        redisTokenService.deleteRefreshToken(user.getEmail());
    }

    // 회원 탈퇴(실제로는 상태 변경)
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUser.getUserById(userId);
        user.setNickname(null);
        user.setStatus(UserStatus.INACTIVE.getStatus());
        user.setDeleteUserDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    // 헤더에서 "Bearer " 제거 후 토큰 추출
    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
