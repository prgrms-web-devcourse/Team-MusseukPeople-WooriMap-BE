package com.musseukpeople.woorimap.auth.application.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private MemberResponse member;

    public TokenResponse(String accessToken, String refreshToken, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = memberResponse;
    }
}
