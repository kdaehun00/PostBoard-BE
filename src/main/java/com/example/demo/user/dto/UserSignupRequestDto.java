package com.example.demo.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
}
