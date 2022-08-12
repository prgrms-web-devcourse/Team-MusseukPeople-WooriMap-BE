package com.musseukpeople.woorimap.auth.presentation;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.common.base.Strings;
import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.application.BlackListService;
import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.common.util.AuthorizationExtractor;
import com.musseukpeople.woorimap.notification.presentation.resolver.LoginRequiredForNoti;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String TOKEN_PARAMETER_NAME = "token";

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (CorsUtils.isPreFlightRequest(request) || isNotLoginRequired(handler)) {
            return true;
        }

        String accessToken = getAccessToken(request, handler);
        validateAccessToken(accessToken);
        validateBlackList(accessToken);
        return true;
    }

    private String getAccessToken(HttpServletRequest request, Object handler) {
        LoginRequiredForNoti auth = ((HandlerMethod)handler).getMethodAnnotation(LoginRequiredForNoti.class);
        if (Objects.nonNull(auth)) {
            return request.getParameter(TOKEN_PARAMETER_NAME);
        }
        return AuthorizationExtractor.extract(request);
    }

    private void validateBlackList(String accessToken) {
        if (blackListService.isBlackList(accessToken)) {
            throw new UnauthorizedException(ErrorCode.BLACKLIST_TOKEN);
        }
    }

    private boolean isNotLoginRequired(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LoginRequired auth = ((HandlerMethod)handler).getMethodAnnotation(LoginRequired.class);
        return Objects.isNull(auth);
    }

    private void validateAccessToken(String accessToken) {
        if (Strings.isNullOrEmpty(accessToken)) {
            throw new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN);
        }

        if (!jwtProvider.validateToken(accessToken)) {
            throw new InvalidTokenException(accessToken, ErrorCode.INVALID_TOKEN);
        }
    }
}
