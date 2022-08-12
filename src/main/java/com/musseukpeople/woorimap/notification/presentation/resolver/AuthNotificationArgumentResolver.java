package com.musseukpeople.woorimap.notification.presentation.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.base.Strings;
import com.musseukpeople.woorimap.auth.aop.MemberAuthorityContext;
import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthNotificationArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_PARAMETER_NAME = "token";

    private final JwtProvider jwtProvider;
    private final MemberAuthorityContext memberAuthorityContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginForNoti.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = httpServletRequest.getParameter(TOKEN_PARAMETER_NAME);
        if (Strings.isNullOrEmpty(accessToken)) {
            return new LoginMember(LoginMember.Authority.ANONYMOUS);
        }

        LoginMember loginMember = getLoginMember(accessToken);
        memberAuthorityContext.setAuthority(loginMember.getAuthority());
        return loginMember;
    }

    private LoginMember getLoginMember(String accessToken) {
        Claims claims = jwtProvider.getClaims(accessToken);
        Long id = convertMemberId(accessToken, claims);
        Long coupleId = convertCoupleId(accessToken, claims);
        return new LoginMember(id, coupleId, accessToken);
    }

    private Long convertMemberId(String token, Claims claims) {
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new InvalidTokenException(token, ErrorCode.INVALID_TOKEN);
        }
    }

    private Long convertCoupleId(String token, Claims claims) {
        try {
            return claims.get(jwtProvider.getClaimName(), Long.class);
        } catch (RequiredTypeException e) {
            throw new InvalidTokenException(token, ErrorCode.INVALID_TOKEN);
        }
    }
}
