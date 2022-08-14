package com.musseukpeople.woorimap.couple.application.dto.response;

import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleMemeberResponse {

    @Schema(description = "프로필 사진 경로")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    public CoupleMemeberResponse(String imageUrl, String nickName) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }

    public static CoupleMemeberResponse from(Member member) {
        return new CoupleMemeberResponse(member.getImageUrl(), member.getNickName().getValue());
    }
}
