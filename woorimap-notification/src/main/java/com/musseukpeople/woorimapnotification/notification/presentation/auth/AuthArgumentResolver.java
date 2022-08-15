package com.musseukpeople.woorimapnotification.notification.presentation.auth;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.musseukpeople.woorimapnotification.common.util.AuthorizationExtractor;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.login.Login;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.login.LoginMember;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.login.LoginRequiredForNoti;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_PARAMETER_NAME = "token";

    private final WoorimapClient woorimapClient;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = getAccessToken(httpServletRequest, parameter);
        LoginMember loginMember = woorimapClient.getLoginMember(accessToken).getData();
        return loginMember;
    }

    private String getAccessToken(HttpServletRequest request, MethodParameter parameter) {
        LoginRequiredForNoti auth = parameter.getMethodAnnotation(LoginRequiredForNoti.class);
        if (Objects.nonNull(auth)) {
            return AuthorizationExtractor.extractQueryString(request);
        }
        return AuthorizationExtractor.extract(request);
    }
}
