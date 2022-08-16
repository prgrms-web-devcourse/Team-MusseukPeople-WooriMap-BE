package com.musseukpeople.woorimap.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponse {

    @Schema(description = "프로필 이미지 URL")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    public ProfileResponse(String imageUrl, String nickName) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }
}
