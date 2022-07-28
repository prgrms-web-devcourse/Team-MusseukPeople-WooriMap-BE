package com.musseukpeople.woorimap.auth.application.dto.response;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String accessToken;
    private final String refreshToken;
    private final MemberResponse member;

    public TokenResponse(String accessToken, String refreshToken, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = memberResponse;
    }
}
