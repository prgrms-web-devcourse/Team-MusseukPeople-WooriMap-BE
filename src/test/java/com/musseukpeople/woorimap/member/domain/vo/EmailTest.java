package com.musseukpeople.woorimap.member.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @DisplayName("이메일 생성 성공")
    @ParameterizedTest(name = "이메일 : {0}")
    @ValueSource(strings = {"test@gmail.com", "woorimap@naver.com", "hwan@daum.com"})
    void createEmail_success(String value) {
        // given
        // when
        Email email = new Email(value);

        // then
        assertThat(email).isNotNull();
    }

    @DisplayName("이메일 생성 빈 값으로 인한 실패")
    @ParameterizedTest(name = "이메일 : {0}")
    @NullAndEmptySource
    void createEmail_nullOrBlank_fail(String value) {
        assertThatThrownBy(() -> new Email(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이메일은 비어있을 수 없습니다.");
    }

    @DisplayName("이메일 생성 최대 글자수 초과로 인한 실패")
    @Test
    void createEmail_overMaxLength_fail() {
        // given
        String value = "test@gmail.com" + "a".repeat(37);

        // when
        // then
        assertThatThrownBy(() -> new Email(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("글자를 초과할 수 없습니다.");
    }

    @DisplayName("이메일 생성 이메일 형식 불일치로 인한 실패")
    @ParameterizedTest(name = "이메일 : {0}")
    @ValueSource(strings = {"test@nnnnn", "woorimap", "test@.com"})
    void createEmail_invalidEmail_fail(String value) {
        assertThatThrownBy(() -> new Email(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 이메일 형식이 아닙니다.");
    }
}
