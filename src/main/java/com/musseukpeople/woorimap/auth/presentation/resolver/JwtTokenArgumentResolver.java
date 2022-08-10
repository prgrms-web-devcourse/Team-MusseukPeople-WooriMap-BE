package com.musseukpeople.woorimap.auth.presentation.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.base.Strings;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.common.util.AuthorizationExtractor;
import com.musseukpeople.woorimap.common.util.CookieUtil;

public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {

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
        return new JwtToken(accessToken, refreshToken);
    }

    private String getAccessTokenByRequest(HttpServletRequest httpServletRequest) {
        String token = AuthorizationExtractor.extract(httpServletRequest);
        if (Strings.isNullOrEmpty(token)) {
            throw new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN);
        }
        return token;
    }
}
