package com.musseukpeople.woorimap.couple.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CoupleTest {

	@DisplayName("커플 생성 성공")
	@Test
	void construct_success() {
		//given
		LocalDate startDate = LocalDate.now();

		//when
		Couple couple = new Couple(startDate);

		//then
		System.out.println(couple.getStartDate());
	}

	@DisplayName("현재 이후 날짜로 인한 생성 실패")
	@ParameterizedTest
	@ValueSource(
		ints = {1, 22, 333, 4444, 55555}
	)
	void couple_afterNowDate_fail(int plusDateCount) {
		//given
		LocalDate afterNowDate = LocalDate.now().plusDays(plusDateCount);

		//when
		assertThatThrownBy(() -> new Couple(afterNowDate))

			//then
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("현재 이후 날짜로 커플을 생성할 수 없습니다.");
	}
}
