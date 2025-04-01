package com.example.demo.user.controller;

// Class
import com.example.demo.user.dto.ChangeUserInfoRequestDTO;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.service.ChangeUserInfoService;

// Framework
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class ChangeUserInfoController {
    private final ChangeUserInfoService changeUserInfoService;


    @GetMapping("/{userId}")
    public ResponseEntity<UserApiResponseDto> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok().body(changeUserInfoService.getUserInfo(userId));
    }


    @PutMapping("/{userId}/info/nickname")
    public ResponseEntity<Object> changeUserNickname(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        changeUserInfoService.changeUserNickname(userId, nickname);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/info/profileImg")
    public ResponseEntity<Object> changeUserProfileImg(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String profileImg = request.get("profileImg");
        changeUserInfoService.changeUserProfileImg(userId, profileImg);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{userId}/info/password")
    public ResponseEntity<UserApiResponseDto> changeUserInfoPassword(@PathVariable Long userId, @RequestBody ChangeUserInfoRequestDTO userInfoDTO) {
        changeUserInfoService.changeUserInfoPassword(userId, userInfoDTO);
        return ResponseEntity.noContent().build();
    }
}