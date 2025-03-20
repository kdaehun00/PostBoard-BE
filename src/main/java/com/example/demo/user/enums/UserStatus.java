package com.example.demo.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

}
