package com.musseukpeople.woorimap.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

	@DisplayName("이메일생성 성공")
	@ParameterizedTest(name = "value : {0}")
	@ValueSource(strings = {"test@gmail.com", "woorimap@naver.com", "hwan@daum.com"})
	void createEmail_success(String value) {
		// given
		// when
		Email email = new Email(value);

		// then
		assertThat(email).isNotNull();
	}

	@DisplayName("이메일생성 실패 - 비어있으면 안된다.")
	@ParameterizedTest
	@NullAndEmptySource
	void createEmail_fail_nullOrBlank(String value) {
		assertThatThrownBy(() -> new Email(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이메일은 비어있을 수 없습니다.");
	}

	@DisplayName("이메일생성 실패 - 최대 글자수 초과")
	@Test
	void createEmail_fail_overMaxLength() {
		// given
		String value = "test@gmail.com" + "a".repeat(37);

		// when
		// then
		assertThatThrownBy(() -> new Email(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("글자를 초과할 수 없습니다.");
	}

	@DisplayName("이메일생성 실패 - 이메일 형식 불일치")
	@ParameterizedTest(name = "value : {0}")
	@ValueSource(strings = {"test@nnnnn", "woorimap", "test@.com"})
	void createEmail_fail_notCorrectEmail(String value) {
		assertThatThrownBy(() -> new Email(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("올바른 이메일 형식이 아닙니다.");
	}
}
