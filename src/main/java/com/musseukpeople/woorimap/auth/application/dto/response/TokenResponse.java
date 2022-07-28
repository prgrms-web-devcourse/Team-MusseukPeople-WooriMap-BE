package com.musseukpeople.woorimap.auth.application.dto.response;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String accessToken;
    private final String refreshToken;
    private final MemberResponse memberResponse;

    public TokenResponse(String accessToken, String refreshToken, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberResponse = memberResponse;
    }

    @Getter
    public static class MemberResponse {

        private final String email;
        private final String nickName;

        public MemberResponse(String email, String nickName) {
            this.email = email;
            this.nickName = nickName;
        }
    }
}
