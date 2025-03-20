package com.example.demo.user.enums;

import lombok.Getter;

@Getter
public enum UserIdPwMessage {
    ALREADY_USE_EMAIL("이미 사용중인 이메일입니다."),
    ALREADY_USE_NICKNAME("이미 사용중인 닉네임입니다."),
    NON_EXIST_USER("회원이 존재하지 않습니다."),
    WRONG_PASSWORD("비밀번호가 틀렸습니다."),
    SUCCESS_CREATE_USER("회원가입 성공"),
    SUCCESS_JOIN_USER("로그인 성공"),
    SUCCESS_CHANGE_INFO("회원 정보 변경 완료");



    private final String message;

    UserIdPwMessage(String message) {
        this.message = message;
    }

}
