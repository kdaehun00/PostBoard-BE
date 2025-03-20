package com.example.demo.user.service;

// Class
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.ChangeUserInfoRequestDTO;
import com.example.demo.user.enums.UserIdPwMessage;
import com.example.demo.user.repository.JPAUserRepository;
import com.example.demo.user.dto.UserApiResponseDto;

//enum

//framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ChangeUserInfoService {

    JPAUserRepository jpaUserRepository;

    @Autowired
    public ChangeUserInfoService(JPAUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    // 사용자 정보 변경시 기존 정보 가져오기
    /* 실제로는 로그인시 사용자의 정보를 다 넘겨서 프론트에서 localStorage에 저장한 뒤, 그 정보를 가져다가 쓰는데
        혹시 몰라서 Get을 만들어놓긴 했습니다.
        사용자가 정보 변경시 local에 있는 정보를 바로 가져오는 게 나은지
        Get을 통해 db에서 직접 가져오는 게 나은지 모르겠습니다.
     */
    public UserApiResponseDto getUserInfo(Long userId) {
        User user = getUserById(userId);
        return new UserApiResponseDto(user.getNickname(), user.getProfileImage());
    }

    @Transactional
    public void changeUserNickname(Long userId, String nickname) {
        if (jpaUserRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException(UserIdPwMessage.ALREADY_USE_NICKNAME.getMessage());
        }
        getUserById(userId).setNickname(nickname);
    }

    @Transactional
    public void changeUserProfileImg(Long userId, String profileImg) {
        getUserById(userId).setProfileImage(profileImg);
    }

    @Transactional
    public void changeUserInfoPassword(Long userId, ChangeUserInfoRequestDTO userInfoDTO) {
        getUserById(userId).setPassword(userInfoDTO.getNewPassword());
    }

    public User getUserById(Long userId) {
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(UserIdPwMessage.NON_EXIST_USER.getMessage()));
    }
}