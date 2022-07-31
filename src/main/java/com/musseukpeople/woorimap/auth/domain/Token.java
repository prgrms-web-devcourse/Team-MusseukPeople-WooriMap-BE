package com.musseukpeople.woorimap.auth.domain;

import lombok.Getter;

@Getter
public class Token {

    private final String id;
    private final String refreshToken;
    private final long expiredTime;

    public Token(String id, String refreshToken, long expiredTime) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }
}
