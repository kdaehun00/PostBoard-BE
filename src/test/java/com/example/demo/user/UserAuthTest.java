package com.example.demo.user;

import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.service.UserAuthService;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// JUnit 5 + Mockito 사용
@Slf4j
@ExtendWith(MockitoExtension.class)
class UserAuthTest {

    // 테스트 대상을 설정
    @InjectMocks
    private UserAuthService userAuthService;

    // 가짜 Repository 생성
    @Mock
    private JPAUserRepository jpaUserRepository;

    // join 메서드 (login)
    @Test
    void userLoginTest() {
        log.info("로그인 성공 테스트");
        // Given
        // 실제 Db의 데이터라고 가정
        User user = User.builder()
                .email("test@example.com")
                .password("test")
                .nickname("test")
                .status(UserStatus.ACTIVE.getStatus())
                .profileImage(null)
                .build();

        // 요청하면 실제 db말고 Mock 데이터에서 꺼내가기
        when(jpaUserRepository.getUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));


        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("test@example.com");
        userLoginRequestDto.setPassword("test");

        // When
        UserApiResponseDto userApiResponseDto = userAuthService.join(userLoginRequestDto);

        // Then
        assertThat(userApiResponseDto.getEmail()).isEqualTo(user.getEmail());
        log.info("테스트 완료 결과: {}", userApiResponseDto.isSuccess());
    }

    @Test
    void NonExistUser(){
        log.info("로그인 실패 테스트");
        // 사용자 존재 X
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("NotFoundUser@example.com");
        loginRequest.setPassword("NotFoundUser");

        when(jpaUserRepository.getUserByEmail("NotFoundUser@example.com")).thenReturn(Optional.empty());

        //
        assertThatThrownBy(() -> userAuthService.join(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.NON_EXIST_USER.getMessage());
        log.info("테스트 완료");
    }


    // registerUser 메서드 (Signup)
    @Test
    void signupTest(){
        log.info("회원가입 성공 테스트");

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("test@example.com");
        userSignupRequestDto.setPassword("test");
        userSignupRequestDto.setNickname("test");
        userSignupRequestDto.setProfileImg(null);


    }

    @Test
    void AlreadyExistEmail(){
        log.info("중복 이메일 감지 테스트");
        // given
        // 회원가입 요청
        UserSignupRequestDto signupRequest = new UserSignupRequestDto();
        signupRequest.setEmail("registerUser@example.com");


        // repository에 이미 있음.
        when(jpaUserRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // then
        assertThatThrownBy(() -> userAuthService.registerUser(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_EMAIL.getMessage());
    }


    @Test
    void AlreadyExistNickname(){
        log.info("중복 닉네임 감지 테스트");
        // given
        UserSignupRequestDto signupRequest = new UserSignupRequestDto();
        signupRequest.setNickname("registerUser");
        when(jpaUserRepository.existsByNickname(signupRequest.getNickname())).thenReturn(true);

        // then
        assertThatThrownBy(() -> userAuthService.registerUser(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
        log.info("테스트 완료");
    }
}
