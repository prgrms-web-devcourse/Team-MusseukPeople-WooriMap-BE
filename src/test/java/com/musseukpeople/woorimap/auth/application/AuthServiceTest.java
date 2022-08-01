package com.musseukpeople.woorimap.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.auth.application.dto.request.RefreshTokenRequest;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;
import com.musseukpeople.woorimap.util.IntegrationTest;

class AuthServiceTest extends IntegrationTest {

    private final String email = "woorimap@gmail.com";
    private final String password = "!Hwan123";
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService.createMember(new SignupRequest(email, password, "test"));
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() {
        // given
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        TokenResponse tokenResponse = authService.login(signInRequest);

        // then
        assertAll(
            () -> assertThat(tokenResponse.getAccessToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getRefreshToken()).isNotBlank()
        );
    }

    @DisplayName("저장되지 않은 이메일로 인한 로그인 실패")
    @Test
    void login_notFoundEmail_fail() {
        // given
        String notFoundEmail = "invalid@gmail.com";
        SignInRequest signInRequest = new SignInRequest(notFoundEmail, password);

        // when
        // then
        assertThatThrownBy(() -> authService.login(signInRequest))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("비밀번호 불일치로 인한 로그인 실패")
    @Test
    void login_invalidPassword_fail() {
        // given
        String invalidPassword = "!Test123";
        SignInRequest signInRequest = new SignInRequest(email, invalidPassword);

        // when
        // then
        assertThatThrownBy(() -> authService.login(signInRequest))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다");
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void refreshAccessToken_success() {
        // given
        TokenResponse tokenResponse = login(email, password);
        String accessToken = tokenResponse.getAccessToken();
        RefreshTokenRequest refreshToken = new RefreshTokenRequest(tokenResponse.getRefreshToken());

        // when
        AccessTokenResponse accessTokenResponse = authService.refreshAccessToken(accessToken, refreshToken);

        // then
        assertThat(accessTokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("유효하지 않는 accessToken으로 인한 재발급 실패")
    @Test
    void refreshAccessToken_invalidAccessToken_fail() {
        // given
        TokenResponse tokenResponse = login(email, password);
        String invalidAccessToken = tokenResponse.getAccessToken() + "invalid";
        RefreshTokenRequest refreshToken = new RefreshTokenRequest(tokenResponse.getRefreshToken());

        // when
        // then
        assertThatThrownBy(() -> authService.refreshAccessToken(invalidAccessToken, refreshToken))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    @DisplayName("유효하지 않는 accessToken으로 인한 재발급 실패")
    @Test
    void refreshAccessToken_invalidRefreshToken_fail() {
        // given
        TokenResponse tokenResponse = login(email, password);
        String invalidAccessToken = tokenResponse.getAccessToken();
        RefreshTokenRequest invalidRefreshToken = new RefreshTokenRequest(tokenResponse.getRefreshToken() + "invalid");

        // when
        // then
        assertThatThrownBy(() -> authService.refreshAccessToken(invalidAccessToken, invalidRefreshToken))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    private TokenResponse login(String email, String password) {
        return authService.login(new SignInRequest(email, password));
    }
}
