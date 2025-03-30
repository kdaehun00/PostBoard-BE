package com.example.demo.controller.user;

import com.example.demo.user.controller.AuthController;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.TokenResponseDto;
import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.dto.UserLoginRequestDto;
import com.example.demo.user.dto.UserSignupRequestDto;
import com.example.demo.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void loginTest() throws Exception {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());

        UserApiResponseDto responseDto = new UserApiResponseDto(user, "accessToken", "refreshToken");
        responseDto.setAccessToken("accessToken");
        responseDto.setRefreshToken("refreshToken");

        when(authService.login(any(UserLoginRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/login")
                        .with(csrf())  // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void refreshTokenTest() throws Exception {
        // given
        TokenResponseDto tokenResponseDto = new TokenResponseDto("newAccessToken", "refreshToken");
        when(authService.refreshToken(anyString())).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/token/refresh")
                        .header("Authorization", "Bearer refreshToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void logoutTest() throws Exception {
        // given
        doNothing().when(authService).logout(anyString());

        // when & then
        mockMvc.perform(delete("/logout")
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isNoContent());
    }

    @Test
    void signupTest() throws Exception {
        // given
        UserSignupRequestDto signupRequest = new UserSignupRequestDto();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setNickname("testNickname");
        signupRequest.setProfileImg("profile.png");

        when(authService.registerUser(any(UserSignupRequestDto.class))).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/signup/1"));
    }

    @Test
    void deleteUserTest() throws Exception {
        // given
        doNothing().when(authService).deleteUser(anyLong());

        // when & then
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
