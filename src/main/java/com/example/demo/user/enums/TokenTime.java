package com.example.demo.user.enums;

import lombok.Getter;

@Getter
public enum TokenTime {
    REFRESH_EXP(1000L * 60 * 60 * 24 * 7);


    public final long expire;

    TokenTime(long expire) {
        this.expire = expire;
    }
}
