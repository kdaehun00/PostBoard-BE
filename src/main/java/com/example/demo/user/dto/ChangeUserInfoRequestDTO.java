package com.example.demo.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeUserInfoRequestDTO {
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profileImg")
    private String profileImg;

    private String currentPassword;

    @JsonProperty("newPassword")
    private String newPassword;
}
