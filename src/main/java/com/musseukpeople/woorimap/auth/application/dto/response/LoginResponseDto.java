package com.musseukpeople.woorimap.auth.application.dto.response;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final TokenDto accessToken;
    private final TokenDto refreshToken;
    private final LoginMemberResponse member;

    public LoginResponseDto(TokenDto accessToken, TokenDto refreshToken, LoginMemberResponse loginMemberResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = loginMemberResponse;
    }
}
