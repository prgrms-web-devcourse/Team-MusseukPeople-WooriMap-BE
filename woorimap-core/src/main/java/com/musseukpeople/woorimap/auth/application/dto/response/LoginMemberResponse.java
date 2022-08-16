package com.musseukpeople.woorimap.auth.application.dto.response;

import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMemberResponse {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    @Schema(description = "커플 유무")
    private Boolean isCouple;

    public LoginMemberResponse(String email, String imageUrl, String nickName, boolean isCouple) {
        this.email = email;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.isCouple = isCouple;
    }

    public static LoginMemberResponse from(Member member) {
        return new LoginMemberResponse(
            member.getEmail().getValue(),
            member.getImageUrl(),
            member.getNickName().getValue(),
            member.isCouple()
        );
    }
}
