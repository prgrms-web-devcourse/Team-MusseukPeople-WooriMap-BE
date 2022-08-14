package com.musseukpeople.woorimap.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenRequestException;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class AuthFacadeTest extends IntegrationTest {

    private final String email = "woorimap@gmail.com";
    private final String password = "!Hwan123";

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new TMemberBuilder().email(email).password(password).build();
        memberRepository.save(member);
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() {
        // given
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        LoginResponseDto loginResponseDto = authFacade.login(signInRequest);

        // then
        assertAll(
            () -> assertThat(loginResponseDto.getAccessToken().getValue()).isNotBlank(),
            () -> assertThat(loginResponseDto.getRefreshToken().getValue()).isNotBlank()
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
        assertThatThrownBy(() -> authFacade.login(signInRequest))
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
        assertThatThrownBy(() -> authFacade.login(signInRequest))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다");
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void refreshAccessToken_success() {
        // given
        LoginResponseDto loginResponseDto = login(email, password);
        String accessToken = loginResponseDto.getAccessToken().getValue();
        String refreshToken = loginResponseDto.getRefreshToken().getValue();

        // when
        AccessTokenResponse accessTokenResponse = authFacade.refreshAccessToken(accessToken, refreshToken);

        // then
        assertThat(accessTokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("유효하지 않는 accessToken으로 인한 재발급 실패")
    @Test
    void refreshAccessToken_invalidAccessToken_fail() {
        // given
        LoginResponseDto loginResponseDto = login(email, password);
        String invalidAccessToken = loginResponseDto.getAccessToken().getValue() + "invalid";
        String refreshToken = loginResponseDto.getRefreshToken().getValue();

        // when
        // then
        assertThatThrownBy(() -> authFacade.refreshAccessToken(invalidAccessToken, refreshToken))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    @DisplayName("유효하지 않는 refreshToken으로 인한 재발급 실패")
    @Test
    void refreshAccessToken_invalidRefreshToken_fail() {
        // given
        LoginResponseDto loginResponseDto = login(email, password);
        String accessToken = loginResponseDto.getAccessToken().getValue();
        String invalidRefreshToken = loginResponseDto.getRefreshToken().getValue() + "invalid";

        // when
        // then
        assertThatThrownBy(() -> authFacade.refreshAccessToken(accessToken, invalidRefreshToken))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    @DisplayName("커플 상태 변환 시 토큰 재발급 성공")
    @Test
    void refreshCoupleAccessToken_success() {
        // given
        LoginResponseDto loginResponseDto = login(email, password);
        String accessToken = loginResponseDto.getAccessToken().getValue();
        String refreshToken = loginResponseDto.getRefreshToken().getValue();
        makeCouple();

        // when
        AccessTokenResponse accessTokenResponse = authFacade.refreshCoupleAccessToken(accessToken, refreshToken);

        // then
        assertThat(accessTokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("커플 상태 변화 없음로 인한 토큰 재발급 실패")
    @Test
    void refreshCoupleAccessToken_couple_fail() {
        // given
        LoginResponseDto loginResponseDto = login(email, password);
        String accessToken = loginResponseDto.getAccessToken().getValue();
        String refreshToken = loginResponseDto.getRefreshToken().getValue();

        // when
        // then
        assertThatThrownBy(() -> authFacade.refreshCoupleAccessToken(accessToken, refreshToken))
            .isInstanceOf(InvalidTokenRequestException.class)
            .hasMessageContaining("토큰을 재발급 할 수 없는 상태입니다.");
    }

    @DisplayName("솔로 상태 변화 없음으로 인한 토큰 재발급 실패")
    @Test
    void refreshCoupleAccessToken_solo_success() {
        // given
        makeCouple();
        LoginResponseDto loginResponseDto = login(email, password);
        String accessToken = loginResponseDto.getAccessToken().getValue();
        String refreshToken = loginResponseDto.getRefreshToken().getValue();

        // when
        // then
        assertThatThrownBy(() -> authFacade.refreshCoupleAccessToken(accessToken, refreshToken))
            .isInstanceOf(InvalidTokenRequestException.class)
            .hasMessageContaining("토큰을 재발급 할 수 없는 상태입니다.");
    }

    private LoginResponseDto login(String email, String password) {
        return authFacade.login(new SignInRequest(email, password));
    }

    private void makeCouple() {
        Member opponentMember = new TMemberBuilder().email("test1@gmail.com").build();
        memberRepository.save(opponentMember);
        List<Member> members = List.of(member, opponentMember);
        Couple couple = coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
        members.forEach(member -> member.changeCouple(couple));
        memberRepository.saveAll(members);
    }
}
