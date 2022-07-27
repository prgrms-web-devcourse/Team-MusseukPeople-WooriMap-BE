package com.musseukpeople.woorimap.member.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("회원가입 성공")
	@Test
	void signup_success() throws Exception {
		// given
		SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!@#asdasd", "hwan");

		// when
		MockHttpServletResponse response = mockMvc.perform(post("/api/members/signup")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(signupRequest)))
			.andDo(print())
			.andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
}
