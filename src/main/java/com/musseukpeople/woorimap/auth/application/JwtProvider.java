package com.musseukpeople.woorimap.auth.application;

import java.util.Date;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;

import io.jsonwebtoken.Claims;

public interface JwtProvider {

    TokenDto createAccessToken(String payload, Long coupleId);

    TokenDto createRefreshToken();

    boolean validateToken(String token);

    Date getExpiredDate(String token);

    Claims getClaims(String token);

    String getClaimName();
}
