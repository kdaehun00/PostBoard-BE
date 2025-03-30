package com.example.demo.service.user;

import com.example.demo.user.domain.User;
import com.example.demo.user.dto.TokenResponseDto;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.enums.TokenTime;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.service.AuthService;
import com.example.demo.user.utils.GetUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JPAUserRepository jpaUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GetUser getUser;

    @Mock
    private com.example.demo.user.service.JwtTokenProvider jwtTokenProvider;

    @Mock
    private com.example.demo.user.service.RedisTokenService redisTokenService;

    private UserSignupRequestDto signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new UserSignupRequestDto();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setNickname("testNickname");
        signupRequest.setProfileImg("profile.png");
    }

    // 로그인 성공 테스트 (UserApiResponseDto 반환)
    @Test
    void userLoginSuccessTest() {
        log.info("로그인 성공 테스트");

        // given
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = User.builder()
                .email(loginRequest.getEmail())
                .password("encodedPassword")
                .build();

        when(getUser.getUserByEmail(loginRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(user.getEmail())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken()).thenReturn("refreshToken");

        // when
        UserApiResponseDto response = authService.login(loginRequest);

        // then
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");

        verify(redisTokenService, times(1))
                .saveRefreshToken(user.getEmail(), "refreshToken", TokenTime.REFRESH_EXP.getExpire());
    }

    // 사용자 미존재로 인한 로그인 실패 테스트
    @Test
    void nonExistUserTest() {
        log.info("존재하지 않는 사용자 로그인 테스트");

        // given
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("NotFoundUser@example.com");
        loginRequest.setPassword("somePassword");

        when(getUser.getUserByEmail(loginRequest.getEmail()))
                .thenThrow(new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });
        assertThat(exception.getMessage()).isEqualTo(UserIdPwMessage.NON_EXIST_USER.getMessage());
    }

    // 비밀번호가 틀린 경우 로그인 실패 테스트
    @Test
    void wrongPasswordTest() {
        log.info("비밀번호 불일치 테스트");

        // given
        String email = "test@example.com";
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail(email);
        loginRequest.setPassword("wrongPassword");

        User user = User.builder()
                .email(email)
                .password("encodedPassword")
                .nickname("testNickname")
                .build();

        when(getUser.getUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });
        assertThat(exception.getMessage()).isEqualTo(UserIdPwMessage.WRONG_PASSWORD.getMessage());
    }

    // 회원가입 성공 테스트
    @Test
    void signupTest() {
        log.info("회원가입 성공 테스트");

        // given
        when(jpaUserRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(jpaUserRepository.existsByNickname(signupRequest.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // save() 호출 시 자동 증가 id를 시뮬레이션 (ReflectionTestUtils 사용)
        when(jpaUserRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1L);
            return user;
        });

        // when
        Long userId = authService.registerUser(signupRequest);

        // then
        assertThat(userId).isEqualTo(1L);
        verify(jpaUserRepository, times(1)).save(any(User.class));
    }

    // 중복 이메일로 인한 회원가입 실패 테스트
    @Test
    void alreadyExistEmailTest() {
        log.info("중복 이메일 감지 테스트");

        // given
        when(jpaUserRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.registerUser(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_EMAIL.getMessage());
    }

    // 중복 닉네임으로 인한 회원가입 실패 테스트
    @Test
    void alreadyExistNicknameTest() {
        log.info("중복 닉네임 감지 테스트");

        when(jpaUserRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(jpaUserRepository.existsByNickname(signupRequest.getNickname())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.registerUser(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
    }

    // 회원 탈퇴 (상태 변경) 테스트
    @Test
    void deleteUserTest() {
        log.info("회원 탈퇴 테스트");

        // given
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE.getStatus())
                .build();

        when(getUser.getUserById(anyLong())).thenReturn(user);

        // when
        authService.deleteUser(1L);

        // then
        assertThat(user.getNickname()).isNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE.getStatus());
        assertThat(user.getDeleteUserDate()).isNotNull();
    }

    // refreshToken() 메서드 테스트

    // 헤더가 "Bearer " 로 시작하지 않는 경우 -> extractToken()에서 null 반환하여 예외 발생
    @Test
    void refreshToken_invalidHeaderTest() {
        log.info("refreshToken - 잘못된 헤더 테스트 (Bearer 없음)");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken("InvalidHeader");
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid refresh token");
    }

    // 토큰이 유효하지 않은 경우
    @Test
    void refreshToken_invalidTokenTest() {
        log.info("refreshToken - 토큰 유효성 검사 실패 테스트");
        when(jwtTokenProvider.validateToken("token")).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken("Bearer token");
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid refresh token");
    }

    // 유저를 찾지 못한 경우
    @Test
    void refreshToken_userNotFoundTest() {
        log.info("refreshToken - 유저 미존재 테스트");
        String token = "validToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn("notFound@example.com");
        when(jpaUserRepository.findByEmail("notFound@example.com")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken("Bearer " + token);
        });
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    // 저장된 리프레시 토큰과 불일치하는 경우
    @Test
    void refreshToken_tokenMismatchTest() {
        log.info("refreshToken - 리프레시 토큰 불일치 테스트");
        String token = "validToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn("test@example.com");
        User user = User.builder().email("test@example.com").build();
        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        // redis에 저장된 토큰과 다르게 설정
        when(redisTokenService.getRefreshToken("test@example.com")).thenReturn("differentToken");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken("Bearer " + token);
        });
        assertThat(exception.getMessage()).isEqualTo("Refresh token mismatch");
    }

    // refreshToken 성공 테스트
    @Test
    void refreshToken_successTest() {
        log.info("refreshToken - 성공 테스트");
        String token = "validToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn("test@example.com");
        User user = User.builder().email("test@example.com").build();
        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        // redis에 저장된 토큰과 동일하게 설정
        when(redisTokenService.getRefreshToken("test@example.com")).thenReturn(token);
        when(jwtTokenProvider.createAccessToken("test@example.com")).thenReturn("newAccessToken");

        TokenResponseDto response = authService.refreshToken("Bearer " + token);
        assertThat(response.getAccessToken()).isEqualTo("newAccessToken");
        assertThat(response.getRefreshToken()).isEqualTo(token);
    }

    // ===== 추가: logout() 메서드 테스트 =====

    // 로그아웃 - 헤더가 올바르지 않은 경우 (extractToken()가 null 반환)
    @Test
    void logout_invalidHeaderTest() {
        log.info("logout - 잘못된 헤더 테스트 (Bearer 없음)");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.logout("InvalidHeader");
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid access token");
    }

    // 로그아웃 - 토큰은 유효하나, 유저를 찾지 못한 경우
    @Test
    void logout_userNotFoundTest() {
        log.info("logout - 유저 미존재 테스트");
        String token = "validAccessToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn("notFound@example.com");
        when(jpaUserRepository.findByEmail("notFound@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.logout("Bearer " + token);
        });
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    void refreshToken_nullHeaderTest() {
        log.info("refreshToken - null 헤더 테스트");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken(null);
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid refresh token");
    }

    // logout에서 header가 null인 경우 테스트
    @Test
    void logout_nullHeaderTest() {
        log.info("logout - null 헤더로 인한 token null 테스트");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.logout(null);
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid access token");
    }

    @Test
    void logout_invalidTokenTest() {
        log.info("logout - 유효하지 않은 토큰 테스트 (validateToken() false)");
        // "Bearer " 접두어가 붙은 올바른 형식의 header를 전달합니다.
        String token = "invalidToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // header가 "Bearer invalidToken"이므로 extractToken()은 token을 "invalidToken"으로 추출합니다.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.logout("Bearer " + token);
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid access token");
    }

    // 로그아웃 - 성공 케이스
    @Test
    void logout_successTest() {
        log.info("logout - 성공 테스트");
        String token = "validAccessToken";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn("test@example.com");
        User user = User.builder().email("test@example.com").build();
        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // when
        authService.logout("Bearer " + token);

        // then
        verify(redisTokenService, times(1)).deleteRefreshToken("test@example.com");
    }
}
