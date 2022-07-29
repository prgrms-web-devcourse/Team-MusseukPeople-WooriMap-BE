package com.musseukpeople.woorimap.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Schema(description = "멤버 정보")
    private MemberResponse member;

    public TokenResponse(String accessToken, String refreshToken, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = memberResponse;
    }
}
