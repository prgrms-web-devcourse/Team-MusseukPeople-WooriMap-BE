package com.musseukpeople.woorimap.member.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("평문 비밀번호를 암호화한다.")
    @Test
    void encryptPassword_success() {
        // given
        String inputPassword = "!!Hwan1234";

        // when
        Password password = Password.encryptPassword(passwordEncoder, inputPassword);

        // then
        assertThat(password.getValue()).isNotEqualTo(inputPassword);
    }

    @DisplayName("올바르지 않는 비밀번호 글자수로 인한 실패")
    @ParameterizedTest(name = "비밀번호 : {0}")
    @ValueSource(strings = {"!aAbc", "!aasd@asdsd12345678901d"})
    void encryptPassword_invalidLength_fail(String value) {
        assertThatThrownBy(() -> Password.encryptPassword(passwordEncoder, value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("자 이하입니다.");
    }

    @DisplayName("대소문자, 숫자, 특수문자 미포함으로 인한 실패")
    @ParameterizedTest(name = "비밀번호 : {0}")
    @ValueSource(strings = {"gogosing", "GOGOSING", "123123213", "우리맵우리맵우리맵", "!!!!!!!!",
        "gogo1111", "GOGO1111", "!!!!aaaa", "!!!!GOGO", "1111AAAA", "1111!!!!",
        "aaaBBB!!!", "aaa111!!!", "AAA111!!!", "aaabbb111"})
    void encryptPassword_invalidValue_fail(String value) {
        assertThatThrownBy(() -> Password.encryptPassword(passwordEncoder, value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("비밀번호는 대소문자, 숫자, 특수 문자를 포함해야 생성 가능합니다.");
    }

    @DisplayName("암호화된 비밀번호와 평문 비밀번호가 일치하지 않는지 확인한다.")
    @Test
    void isNotSamePassword_success() {
        // given
        String inputPassword = "!!Hwan1234";

        // when
        Password password = Password.encryptPassword(passwordEncoder, inputPassword);

        // then
        assertThat(password.isNotSamePassword(passwordEncoder, inputPassword)).isFalse();
    }
}
