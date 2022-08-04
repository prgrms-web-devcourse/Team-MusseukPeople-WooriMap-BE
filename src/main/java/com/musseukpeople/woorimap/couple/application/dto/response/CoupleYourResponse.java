package com.musseukpeople.woorimap.couple.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleYourResponse {

    @Schema(description = "프로필 사진 경로")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    public CoupleYourResponse(String imageUrl, String nickName) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }
}
