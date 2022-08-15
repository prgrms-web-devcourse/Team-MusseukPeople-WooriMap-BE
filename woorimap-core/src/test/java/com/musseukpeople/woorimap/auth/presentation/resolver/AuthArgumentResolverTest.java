package com.musseukpeople.woorimap.auth.presentation.resolver;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.Authority.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {
        authArgumentResolver = new AuthArgumentResolver(PROVIDER, new MemberAuthorityContext());
    }

    @DisplayName("커플일 시 커플 권한으로 변환 성공")
    @Test
    void resolveArgument_couple_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1", 1L);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());
        NativeWebRequest request = new ServletWebRequest(servletRequest);

        // when
        Object result = authArgumentResolver.resolveArgument(null, null, request, null);

        // then

        assertAll(
            () -> assertThat(result).isInstanceOf(LoginMember.class),
            () -> assertThat(((LoginMember)result).getAuthority()).isEqualTo(COUPLE)
        );
    }

    @DisplayName("솔로일 시 솔로 권한으로 변환 성공")
    @Test
    void resolveArgument_solo_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1", null);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());
        NativeWebRequest request = new ServletWebRequest(servletRequest);

        // when
        Object result = authArgumentResolver.resolveArgument(null, null, request, null);

        // then

        assertAll(
            () -> assertThat(result).isInstanceOf(LoginMember.class),
            () -> assertThat(((LoginMember)result).getAuthority()).isEqualTo(SOLO)
        );
    }

    @DisplayName("로그인을 하지 않았을 시 익명 권한으로 변환 성공")
    @Test
    void resolveArgument_anonymous_success() {
        // given
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        NativeWebRequest request = new ServletWebRequest(servletRequest);

        // when
        Object result = authArgumentResolver.resolveArgument(null, null, request, null);

        // then

        assertAll(
            () -> assertThat(result).isInstanceOf(LoginMember.class),
            () -> assertThat(((LoginMember)result).getAuthority()).isEqualTo(ANONYMOUS)
        );
    }
}
