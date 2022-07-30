package com.musseukpeople.woorimap.member.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입 성공")
    @Test
    void signup_success() throws Exception {
        // given
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!Hwan1234", "hwan");

        // when
        MockHttpServletResponse response = 회원가입(signupRequest);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("중복된 이메일로 인한 회원가입 실패")
    @Test
    void signup_duplicateEmail_fail() throws Exception {
        // given
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!Hwan1234", "hwan");
        회원가입(signupRequest);

        // when
        MockHttpServletResponse response = 회원가입(signupRequest);

        // then
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(errorResponse.getCode()).isEqualTo("U002"),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("중복된 이메일입니다.")
        );
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void withdrawal_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        String accessToken = 로그인_토큰(new SignInRequest(email, password));

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/api/members")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
