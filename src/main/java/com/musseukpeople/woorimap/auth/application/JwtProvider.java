package com.musseukpeople.woorimap.auth.application;

import io.jsonwebtoken.Claims;

public interface JwtProvider {

    String createAccessToken(String payload, Long coupleId);

    String createRefreshToken();

    boolean validateToken(String token);

    Claims getClaims(String token);

    String getClaimName();

    long getRefreshTokenExpiredTime();
}
