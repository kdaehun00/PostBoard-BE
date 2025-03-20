package com.example.demo.user;


import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.service.ChangeUserInfoService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ChangeUserInfoTest {
    @InjectMocks
    private ChangeUserInfoService changeUserInfoService;

    @Mock
    private JPAUserRepository jpaUserRepository;

    @Test
    void SuccessChangeUserNickname() {
        // given
        User user = User.builder()
                .nickname("test")
                .build();

        String newNickname = "test2";
        // getUser을 사용하면 상단의 user가 반환돠도록 함.
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(user));
        changeUserInfoService.changeUserNickname(1L, newNickname);
        assertThat(user.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void FailChangeUserNickname() {
        // given
        String nickname = "existNickname";
        when(jpaUserRepository.existsByNickname(nickname)).thenReturn(true);

        // then
        assertThatThrownBy(() -> changeUserInfoService.changeUserNickname(1L, nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
    }

    @Test
    void SuccessChangeUserProfileImg() {
        // given
        // db 내의 img
        User user = User.builder()
                    .profileImage("oldProfileImg")
                    .build();

        // 새 img
        String profileImg = "newProfileImg";
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        changeUserInfoService.changeUserProfileImg(1L, profileImg);

        // then
        assertThat(user.getProfileImage()).isEqualTo(profileImg);
    }
}
