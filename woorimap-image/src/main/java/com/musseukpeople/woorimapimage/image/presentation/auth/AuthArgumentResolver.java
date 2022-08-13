package com.musseukpeople.woorimapimage.image.presentation.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.musseukpeople.woorimapimage.image.presentation.auth.login.Login;
import com.musseukpeople.woorimapimage.image.presentation.auth.login.LoginMember;
import com.musseukpeople.woorimapimage.image.presentation.auth.login.MemberResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final WoorimapClient woorimapClient;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        MemberResponse memberResponse = woorimapClient.getMemberResponse(accessToken).getData();
        return new LoginMember(memberResponse.getId(), accessToken);
    }
}
