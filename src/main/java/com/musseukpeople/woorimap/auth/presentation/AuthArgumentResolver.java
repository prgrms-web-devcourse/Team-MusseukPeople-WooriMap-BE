package com.musseukpeople.woorimap.auth.presentation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = AuthorizationExtractor.extract(httpServletRequest)
            .orElseThrow(() -> new InvalidTokenException(null, ErrorCode.INVALID_TOKEN));

        return getLoginMember(accessToken);
    }

    private LoginMember getLoginMember(String accessToken) {
        Claims claims = jwtProvider.getClaims(accessToken);
        String id = claims.getSubject();
        Long coupleId = claims.get(jwtProvider.getClaimName(), Long.class);
        return new LoginMember(Long.valueOf(id), coupleId);
    }
}
