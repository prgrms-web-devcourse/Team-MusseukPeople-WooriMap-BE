package com.musseukpeople.woorimap.common.util;

import java.time.Duration;
import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseCookie;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public static ResponseCookie createTokenCookie(TokenDto tokenDto) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokenDto.getValue())
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(Duration.ofMillis(tokenDto.getExpiredTime()))
            .build();
    }

    public static String getTokenCookieValue(HttpServletRequest request) {
        Cookie findCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
            .findAny()
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
        return findCookie.getValue();
    }
}
