package com.musseukpeople.woorimap.auth.application.dto;

import lombok.Getter;

@Getter
public class TokenDto {

    private final String value;
    private final long expiredTime;

    public TokenDto(String value, long expiredTime) {
        this.value = value;
        this.expiredTime = expiredTime;
    }
}
