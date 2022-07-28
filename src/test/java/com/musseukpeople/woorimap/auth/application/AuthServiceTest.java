package com.musseukpeople.woorimap.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;
import com.musseukpeople.woorimap.member.exception.NotFoundMemberException;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        Member member = new TMemberBuilder()
            .email(email)
            .password(password)
            .build();
        memberRepository.save(member);
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
    void login_fail_notFoundEmail() {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        // then
        assertThatThrownBy(() -> authService.login(signInRequest))
            .isInstanceOf(NotFoundMemberException.class)
            .hasMessageContaining("존재하지 않는 회원 이메일입니다.");
    }

    @DisplayName("비밀번호 불일치로 인한 로그인 실패")
    @Test
    void login_fail_invalidPassword() {
        // given
        String email = "woorimap@gmail.com";
        Member member = new TMemberBuilder()
            .email(email)
            .password("!Hwan123")
            .build();
        memberRepository.save(member);
        SignInRequest signInRequest = new SignInRequest(email, "!Test123");

        // when
        // then
        assertThatThrownBy(() -> authService.login(signInRequest))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다");
    }
}
