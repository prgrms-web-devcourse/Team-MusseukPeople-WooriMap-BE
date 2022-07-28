package com.musseukpeople.woorimap.couple.domain;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple {

	private static final LocalDate START_LIMIT_DATE = LocalDate.now().plusDays(1);

	@Id
	@GeneratedValue
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	public Couple(LocalDate startDate) {
		checkArgument(startDate.isBefore(START_LIMIT_DATE),
			"현재 이후 날짜로 커플을 생성할 수 없습니다.");

		this.startDate = startDate;
	}
}
