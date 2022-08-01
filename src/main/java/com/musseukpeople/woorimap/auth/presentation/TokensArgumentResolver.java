package com.musseukpeople.woorimap.auth.presentation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.auth.presentation.dto.RequestTokens;
import com.musseukpeople.woorimap.auth.presentation.dto.Tokens;
import com.musseukpeople.woorimap.auth.presentation.util.CookieUtil;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class TokensArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestTokens.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = getAccessTokenByRequest(request);
        String refreshToken = CookieUtil.getTokenCookieValue(request);
        return new Tokens(accessToken, refreshToken);
    }

    private String getAccessTokenByRequest(HttpServletRequest httpServletRequest) {
        return AuthorizationExtractor.extract(httpServletRequest)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
    }
}
