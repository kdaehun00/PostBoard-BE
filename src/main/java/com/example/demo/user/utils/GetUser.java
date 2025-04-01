package com.example.demo.user.utils;

import com.example.demo.user.domain.User;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUser {
    private final JPAUserRepository jpaUserRepository;

    public User getUserById(long userId){
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
    }

    public User getUserByEmail(String email){
        return jpaUserRepository.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
    }
}
