package com.musseukpeople.woorimap.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.exception.DuplicateEmailException;

@SpringBootTest
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void tearDown() {
		memberRepository.deleteAllInBatch();
	}

	@DisplayName("회원 생성 성공")
	@Test
	void createMember_success() {
		// given
		SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!@Hwan123", "우리맵");

		// when
		Long id = memberService.createMember(signupRequest);

		// then
		assertThat(id).isPositive();
	}

	@DisplayName("중복 이메일로 인한 회원 생성 실패")
	@Test
	void createMember_fail_duplicateEmail() {
		// given
		String email = "test@gmail.com";
		memberService.createMember(new SignupRequest(email, "!@Hwan123", "우리맵"));
		SignupRequest duplicateEmailRequest = new SignupRequest(email, "!@Test123", "우리맵");

		// when
		// then
		assertThatThrownBy(() -> memberService.createMember(duplicateEmailRequest))
			.isInstanceOf(DuplicateEmailException.class);
	}
}
