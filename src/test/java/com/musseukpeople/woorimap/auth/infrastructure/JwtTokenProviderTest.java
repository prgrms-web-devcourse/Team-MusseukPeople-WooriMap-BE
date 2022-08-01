package com.musseukpeople.woorimap.auth.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtTokenProviderTest {

    private static final JwtTokenProvider PROVIDER = new JwtTokenProvider(
        "test",
        "twk4jbz8a6smC4u0Xv6KvQUImMfVZ16/SCR0uKJIv3g=",
        60_000,
        60_000
    );

    @DisplayName("토큰에서 페이로드를 추출한다.")
    @Test
    void createAccessToken_success() {
        // given
        String memberId = "1";
        Long coupleId = 1L;
        String accessToken = PROVIDER.createAccessToken(memberId, coupleId);

        // when
        Claims claims = PROVIDER.getClaims(accessToken);

        // then
        assertAll(
            () -> assertThat(claims.getSubject()).isEqualTo(memberId),
            () -> assertThat(claims.get("coupleId", Long.class)).isEqualTo(coupleId)
        );
    }

    @DisplayName("리프레쉬 토큰 생성 성공")
    @Test
    void createRefreshToken_success() {
        // given
        String refreshToken = PROVIDER.createRefreshToken();

        // when
        String payload = PROVIDER.getClaims(refreshToken).getSubject();

        // then
        assertThat(payload).isNotNull();
    }

    @DisplayName("토큰 유효성 검증 성공")
    @Test
    void validateToken_success() {
        // given
        String token = PROVIDER.createAccessToken("1", null);

        // when

        // then
        assertThat(PROVIDER.validateToken(token)).isTrue();
    }

    @DisplayName("만료기간이 지남으로 인한 유효성 검증 실패")
    @Test
    void validateToken_overExpired_fail() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            "test",
            "IbPja88BzwyzmvvNwOadW8JUZF5MX1vzxfFtlvokPNE=",
            0,
            0
        );
        String token = jwtTokenProvider.createAccessToken("1", null);

        // when
        // then
        assertThat(PROVIDER.validateToken(token)).isFalse();
    }

    @DisplayName("올바르지 않는 시그니처로 인한 유효성 검증 실패")
    @Test
    void validateToken_invalidSignature_fail() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            "test",
            "IbPja88BzwyzmvvNwOadW8JUZF5MX1vzxfFtlvokPNE=",
            60000,
            60000
        );
        String token = PROVIDER.createAccessToken("1", null);

        // when
        // then
        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }
}
