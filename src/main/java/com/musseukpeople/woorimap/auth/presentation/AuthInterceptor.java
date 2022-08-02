package com.musseukpeople.woorimap.auth.presentation;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (CorsUtils.isPreFlightRequest(request) || isNotLoginRequired(handler)) {
            return true;
        }

        String accessToken = AuthorizationExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
        validateAccessToken(accessToken);
        return true;
    }

    private boolean isNotLoginRequired(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LoginRequired auth = ((HandlerMethod)handler).getMethodAnnotation(LoginRequired.class);
        return Objects.isNull(auth);
    }

    private void validateAccessToken(String accessToken) {
        if (!jwtProvider.validateToken(accessToken)) {
            throw new InvalidTokenException(accessToken, ErrorCode.INVALID_TOKEN);
        }
    }
}
