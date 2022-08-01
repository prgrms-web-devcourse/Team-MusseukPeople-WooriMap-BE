package com.musseukpeople.woorimap.auth.presentation.dto;

import lombok.Getter;

@Getter
public class Tokens {

    private final String accessToken;
    private final String refreshToken;

    public Tokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
