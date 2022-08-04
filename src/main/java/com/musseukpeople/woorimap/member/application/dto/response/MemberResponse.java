package com.musseukpeople.woorimap.member.application.dto.response;

import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description = "프로필 이미지 url")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    @Schema(description = "커플 유무")
    private boolean isCouple;

    public MemberResponse(String imageUrl, String nickName, boolean isCouple) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.isCouple = isCouple;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getImageUrl(),
            member.getNickName().getValue(),
            member.isCouple()
        );
    }
}
