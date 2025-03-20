package com.example.demo.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApiResponseDto {
    private boolean success;
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;

    // 프로필 선택시 DataForm으로 받은 뒤 URL로 넘겨줄 때 사용.
    public UserApiResponseDto(String profileImg) {
        this.success = true;
        this.profileImg = profileImg;
    }

    // 로그인 시 사용자 정보 제공 -> LocalStorage 저장용
    public UserApiResponseDto(Long userId, String email, String nickname, String profileImg) {
        this.success = true;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    // 정보 변경시 최신화용으로 사용.
    public UserApiResponseDto(String nickname, String profileImg) {
        this.success = true;
        this.profileImg = profileImg;
        this.nickname = nickname;
    }

    // 로그인시 성공 여부와 userId 반환 (추후 JWT로 변경 예정)
    public UserApiResponseDto(Long userId) {
        this.success = true;
        this.userId = userId;
    }
}
