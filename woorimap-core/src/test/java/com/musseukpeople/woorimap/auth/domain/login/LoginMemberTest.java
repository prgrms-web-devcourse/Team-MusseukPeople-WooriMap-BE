package com.musseukpeople.woorimap.auth.domain.login;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.Authority.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginMemberTest {

    @DisplayName("커플 아이디가 없을 시 솔로 확인 성공")
    @Test
    void isSolo_nullCoupleId_success() {
        // given
        LoginMember loginMember = new LoginMember(1L, null, "accessToken");

        // when
        boolean result = loginMember.isSolo();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("커플 아이디가 있을 시 커플 확인 성공")
    @Test
    void isCouple_existCoupleId_success() {
        // given
        LoginMember loginMember = new LoginMember(1L, 1L, "accessToken");

        // when
        boolean result = loginMember.isCouple();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("익명 확인 성공")
    @Test
    void isAnonymous_success() {
        // given
        LoginMember loginMember = new LoginMember(ANONYMOUS);

        // when
        boolean result = loginMember.isAnonymous();

        // then
        assertThat(result).isTrue();
    }
}
