package com.example.demo.service.user;


import com.example.demo.user.domain.User;
import com.example.demo.user.dto.ChangeUserInfoRequestDTO;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.service.ChangeUserInfoService;
import com.example.demo.user.utils.GetUser;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ChangeUserInfoTest {
    @InjectMocks
    private ChangeUserInfoService service;

    @Mock
    private JPAUserRepository jpaUserRepository;

    @Mock
    private GetUser getUser;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("test")
                .nickname("test")
                .profileImage("image.png")
                .build();
    }

    @Test
    void getUserInfo(){
        // given
        when(getUser.getUserById(anyLong())).thenReturn(user);

        // when
        UserApiResponseDto userApiResponseDto = service.getUserInfo(1L);

        // then
        assertThat(userApiResponseDto.getNickname()).isEqualTo(user.getNickname());
        assertThat(userApiResponseDto.getProfileImg()).isEqualTo(user.getProfileImage());
    }

    @Test
    void 닉네임_바꾸기_실패(){
        // given
        when(jpaUserRepository.existsByNickname(anyString())).thenReturn(Boolean.TRUE);

        // then
        assertThatThrownBy(() -> service.changeUserNickname(1L, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
    }


    @Test
    void 닉네임_바꾸기_성공() {
        // given
        when(jpaUserRepository.existsByNickname(anyString())).thenReturn(Boolean.FALSE);
        when(getUser.getUserById(anyLong())).thenReturn(user);

        // when
        service.changeUserNickname(1L, "test2");

        // then
        assertThat(user.getNickname()).isEqualTo("test2");
    }


    @Test
    void 프로필_변경_성공() {
        // given
        String profileImg = "newProfileImg";
        when(getUser.getUserById(anyLong())).thenReturn(user);

        // when
        service.changeUserProfileImg(1L, profileImg);

        // then
        assertThat(user.getProfileImage()).isEqualTo(profileImg);
    }

    @Test
    void 비밀번호_변경_성공() {
        // given
        ChangeUserInfoRequestDTO userInfoRequestDTO = new ChangeUserInfoRequestDTO();
        userInfoRequestDTO.setNewPassword("newPassword");
        when(getUser.getUserById(anyLong())).thenReturn(user);

        // when
        service.changeUserInfoPassword(1L, userInfoRequestDTO);

        // then
        assertThat(user.getPassword()).isEqualTo(userInfoRequestDTO.getNewPassword());
        assertThat(user.getPasswordUpdateDate()).isNotNull();
    }
}
