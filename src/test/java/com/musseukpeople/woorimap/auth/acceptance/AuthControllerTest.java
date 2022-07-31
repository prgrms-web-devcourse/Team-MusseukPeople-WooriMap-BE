package com.musseukpeople.woorimap.auth.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.RefreshTokenRequest;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class AuthControllerTest extends AcceptanceTest {

    @BeforeEach
    void setUp() throws Exception {
        회원가입(new SignupRequest("woorimap@gmail.com", "!Hwan123", "hwan"));
    }

    @DisplayName("로그인 성공")
    @Test
    void signIn_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        MockHttpServletResponse response = 로그인(signInRequest);

        // then
        TokenResponse tokenResponse = getResponseObject(response, TokenResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(tokenResponse.getAccessToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getRefreshToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getMember().getEmail()).isEqualTo("woorimap@gmail.com"),
            () -> assertThat(tokenResponse.getMember().getNickName()).isEqualTo("hwan")
        );
    }

    @DisplayName("저장되지 않은 이메일로 인한 로그인 실패")
    @Test
    void signIn_notFoundEmail_fail() throws Exception {
        // given
        String email = "invalid@gmail.com";
        String password = "!Hwan123";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("비밀번호 불일치로 인한 로그인 실패")
    @Test
    void signIn_invalidPassword_fail() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "invalidPassword";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logout_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        String accessToken = 로그인_토큰(new SignInRequest(email, password));

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/auth/signout")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void refreshAccessToken_success() throws Exception {
        // given
        TokenResponse tokenResponse = 로그인_모든_토큰("woorimap@gmail.com", "!Hwan123");

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        // then
        AccessTokenResponse accessTokenResponse = getResponseObject(response, AccessTokenResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(accessTokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("유효하지 않은 accessToken으로 인한 토큰 재발급 실패")
    @Test
    void refreshAccessToken_invalidAccessToken_fail() throws Exception {
        // given
        TokenResponse tokenResponse = 로그인_모든_토큰("woorimap@gmail.com", "!Hwan123");

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(tokenResponse.getAccessToken() + "invalid",
            tokenResponse.getRefreshToken());

        // then
        토큰_재발급_실패(response);
    }

    @DisplayName("유효하지 않은 refreshToken으로 인한 토큰 재발급 실패")
    @Test
    void refreshAccessToken_invalidRefreshToken_fail() throws Exception {
        TokenResponse tokenResponse = 로그인_모든_토큰("woorimap@gmail.com", "!Hwan123");

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(tokenResponse.getAccessToken(),
            tokenResponse.getRefreshToken() + "invalid");

        // then
        토큰_재발급_실패(response);
    }

    private TokenResponse 로그인_모든_토큰(String email, String password) throws Exception {
        SignInRequest signInRequest = new SignInRequest(email, password);
        return getResponseObject(로그인(signInRequest), TokenResponse.class);
    }

    private MockHttpServletResponse 토큰_재발급_요청(String accessToken, String refreshToken) throws Exception {
        return mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(new RefreshTokenRequest(refreshToken))))
            .andReturn().getResponse();
    }

    private void 로그인_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다."),
            () -> assertThat(errorResponse.getCode()).isEqualTo("U001")
        );
    }

    private void 토큰_재발급_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("유효하지 않은 토큰입니다.")
        );
    }
}
