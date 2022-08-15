package com.musseukpeople.woorimap.auth.application;

import java.util.Date;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;

public interface JwtProvider {

    TokenDto createAccessToken(String payload, Long coupleId);

    TokenDto createRefreshToken();

    boolean validateToken(String token);

    Date getExpiredDate(String token);

    String getSubject(String token);

    Long getCoupleId(String token);
}
