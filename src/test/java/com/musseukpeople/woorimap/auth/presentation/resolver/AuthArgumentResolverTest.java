package com.musseukpeople.woorimap.auth.presentation.resolver;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import com.musseukpeople.woorimap.auth.aop.MemberAuthorityContext;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.infrastructure.JwtTokenProvider;

class AuthArgumentResolverTest {

    private static final JwtTokenProvider PROVIDER = new JwtTokenProvider(
        "test",
        "twk4jbz8a6smC4u0Xv6KvQUImMfVZ16/SCR0uKJIv3g=",
        60_000,
        60_000
    );

    private AuthArgumentResolver authArgumentResolver;
    private MemberAuthorityContext memberAuthorityContext;

    @BeforeEach
    void setUp() {
        authArgumentResolver = new AuthArgumentResolver(PROVIDER, memberAuthorityContext);
    }

    @DisplayName("Argument 변환 성공")
    @Test
    void resolveArgument_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1", null);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());
        NativeWebRequest request = new ServletWebRequest(servletRequest);

        // when
        Object result = authArgumentResolver.resolveArgument(null, null, request, null);

        // then
        assertThat(result).isInstanceOf(LoginMember.class);
    }
}
