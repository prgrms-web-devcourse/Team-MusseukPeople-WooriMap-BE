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

    @Schema(description = "닉네임")
    private String nickName;

    public LoginMemberResponse(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public static LoginMemberResponse from(Member member) {
        return new LoginMemberResponse(member.getEmail().getValue(), member.getNickName().getValue());
    }
}
