package com.musseukpeople.woorimap.auth.application.dto;

import lombok.Getter;

@Getter
public class TokenDto {

    private final String id;
    private final String refreshToken;
    private final long expiredTime;

    public TokenDto(String id, String refreshToken, long expiredTime) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }
}
