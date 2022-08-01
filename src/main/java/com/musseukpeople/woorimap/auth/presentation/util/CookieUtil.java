package com.musseukpeople.woorimap.auth.presentation.util;

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

    public static ResponseCookie createTokenCookie(String cookieName, TokenDto tokenDto) {
        return ResponseCookie.from(cookieName, tokenDto.getValue())
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(Duration.ofMillis(tokenDto.getExpiredTime()))
            .build();
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie findCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(cookieName))
            .findAny()
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
        return findCookie.getValue();
    }
}
