package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApiResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;
    private String accessToken;
    private String refreshToken;

    // 프로필 선택시 DataForm으로 받은 뒤 URL로 넘겨줄 때 사용.
    public UserApiResponseDto(String profileImg) {
        this.profileImg = profileImg;
    }

    // 로그인 시 사용자 정보 제공 -> LocalStorage 저장용
    public UserApiResponseDto(User user, String newAccessToken, String refreshToken) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImage();
        this.accessToken = newAccessToken;
        this.refreshToken = refreshToken;
    }

    // 정보 변경시 최신화용으로 사용.
    public UserApiResponseDto(String nickname, String profileImg) {
        this.profileImg = profileImg;
        this.nickname = nickname;
    }
}
