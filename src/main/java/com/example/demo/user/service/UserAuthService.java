package com.example.demo.user.service;

// Class
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.dto.UserApiResponseDto;

//enum
import com.example.demo.user.enums.UserStatus;

//framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserAuthService {

    JPAUserRepository jpaUserRepository;

    @Autowired
    public UserAuthService(JPAUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Transactional(readOnly = true)
    public UserApiResponseDto join(UserLoginRequestDto userLoginRequestDto) {
        User user = jpaUserRepository.getUserByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
        if (!user.getPassword().equals(userLoginRequestDto.getPassword())) {
            throw new IllegalArgumentException(UserIdPwMessage.WRONG_PASSWORD.getMessage());
        }
        return new UserApiResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }

    @CachePut(value = "userCache", key = "#result")
    @Transactional
    public Long registerUser(UserSignupRequestDto userSignupRequestDto) {
        if(jpaUserRepository.existsByEmail(userSignupRequestDto.getEmail())){
            throw new IllegalArgumentException(UserIdPwMessage.ALREADY_USE_EMAIL.getMessage());
        }
        if(jpaUserRepository.existsByNickname(userSignupRequestDto.getNickname())){
            throw new IllegalArgumentException(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
        }
        // Builder 패턴 사용해보았습니다.
        User user = User.builder()
                .email(userSignupRequestDto.getEmail())
                .password(userSignupRequestDto.getPassword())
                .nickname(userSignupRequestDto.getNickname())
                .status(UserStatus.ACTIVE.getStatus())
                .profileImage(userSignupRequestDto.getProfileImg())
                .build();
        jpaUserRepository.save(user);
        return user.getId();
    }

    @CacheEvict(value = "userCache", key = "#userId")
    @Transactional
    public void deleteUser(Long userId) {
        jpaUserRepository.deleteById(userId);
    }
}