package com.musseukpeople.woorimap.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "닉네임")
    private String nickName;

    public MemberResponse(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }
}
