package com.musseukpeople.woorimap.couple.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InviteCodeTest {

    private static final LocalDateTime EXPIRE_DATE = LocalDateTime.now().plusDays(1);

    @DisplayName("초대 코드 생성 성공")
    @ParameterizedTest(name = "초대 코드: {0}")
    @ValueSource(
        strings = {"1234567", "2345678", "1111111", "9999999"}
    )
    void construct_success(String code) {
        //given
        //when
        InviteCode inviteCode = new InviteCode(code, 1L, EXPIRE_DATE);
        //then
        assertAll(
            () -> assertThat(inviteCode.getCode()).hasSize(7),
            () -> assertThat(inviteCode.getExpireDateTime()).isBeforeOrEqualTo(LocalDateTime.now().plusDays(1))
        );
    }

    @DisplayName("길이 제한으로 코드 생성 실패")
    @ParameterizedTest(name = "길이 7이 아닌 코드: {0}")
    @ValueSource(
        strings = {"0", "12", "123", "1234", "12345", "123456", "12345678", "123456789", "1234567890"}
    )
    void construct_lengthNot7_fail(String failCode) {
        //given
        //when
        assertThatThrownBy(() -> new InviteCode(failCode, 1L, EXPIRE_DATE))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("초대 코드의 길이가 다릅니다.");
    }

    @DisplayName("숫자가 아닌 코드로 인한 생성 실패")
    @ParameterizedTest(name = "문자를 가진 코드: {0}")
    @ValueSource(
        strings = {"12b4c6a", "abcdefg", "123456!", "!@#$%^&"}
    )
    void construct_notNumber_fail(String failCode) {
        //given
        //when
        assertThatThrownBy(() -> new InviteCode(failCode, 1L, EXPIRE_DATE))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("초대 코드가 숫자가 아닙니다.");
    }
}
