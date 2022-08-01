package com.musseukpeople.woorimap.auth.presentation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.auth.infrastructure.JwtTokenProvider;

class AuthInterceptorTest {

    private static final JwtTokenProvider PROVIDER = new JwtTokenProvider(
        "test",
        "twk4jbz8a6smC4u0Xv6KvQUImMfVZ16/SCR0uKJIv3g=",
        60_000,
        60_000
    );

    private AuthInterceptor authInterceptor;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptor(PROVIDER);
        response = new MockHttpServletResponse();
    }

    @DisplayName("인터셉터 통과 성공")
    @Test
    void preHandle_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1", null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());

        // when
        boolean result = authInterceptor.preHandle(request, new MockHttpServletResponse(), null);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("CORS 요청일 경우 인터셉터 통과 성공")
    @Test
    void preHandle_cors_success() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.OPTIONS.name());
        request.addHeader(HttpHeaders.ORIGIN, "http://localhost:8080");
        request.addHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET");

        // when
        boolean result = authInterceptor.preHandle(request, new MockHttpServletResponse(), null);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("헤더에 토큰이 없음으로 인한 실패")
    @Test
    void preHandle_notFoundToken_fail() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        // when
        // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, null))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage("로그인이 필요합니다.");
    }

    @DisplayName("유효하지 않은 토큰으로 인한 실패")
    @Test
    void preHandle_invalidToken_fail() {
        // given
        String token = "invalidToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // when
        // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }
}
