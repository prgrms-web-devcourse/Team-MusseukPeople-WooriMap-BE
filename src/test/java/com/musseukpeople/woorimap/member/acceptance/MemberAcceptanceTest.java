package com.musseukpeople.woorimap.member.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.member.application.dto.request.EditProfileRequest;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.application.dto.response.MemberResponse;
import com.musseukpeople.woorimap.member.application.dto.response.ProfileResponse;
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
        String email = "test@gmail.com";
        String password = "!Hwan1234";
        String accessToken = 회원가입_토큰(new SignupRequest(email, password, "hwan"));

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/api/members")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("회원 정보 수정 성공")
    @Test
    void editProfile_success() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        String nickName = "wooriMap";
        String imageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png";
        EditProfileRequest editProfileRequest = new EditProfileRequest(imageUrl, nickName);

        // when
        MockHttpServletResponse response = 프로필_수정(accessToken, editProfileRequest);

        // then
        ProfileResponse profileResponse = getResponseObject(response, ProfileResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(profileResponse.getNickName()).isEqualTo(nickName),
            () -> assertThat(profileResponse.getImageUrl()).isEqualTo(imageUrl)
        );
    }

    @DisplayName("회원 정보 조회 성공")
    @Test
    void showMember_success() throws Exception {
        // given
        String email = "test@gmail.com";
        String password = "!Hwan1234";
        String nickName = "hwan";
        String accessToken = 회원가입_토큰(new SignupRequest(email, password, nickName));

        // when
        MockHttpServletResponse response = 회원_정보_조회(accessToken);

        // then
        MemberResponse memberResponse = getResponseObject(response, MemberResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(memberResponse.getNickName()).isEqualTo(nickName),
            () -> assertThat(memberResponse.getIsCouple()).isFalse()
        );
    }

    @DisplayName("URL이 아닌 아님으로 인한 프로필 수정 실패")
    @Test
    void editProfile_notUrl_fail() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        String imageUrl = "invalidUrl";
        String nickName = "wooriMap";
        EditProfileRequest editProfileRequest = new EditProfileRequest(imageUrl, nickName);

        // when
        MockHttpServletResponse response = 프로필_수정(accessToken, editProfileRequest);

        // then
        이미지_URL_실패(response);
    }

    @DisplayName("이미지 URL이 아닌 아님으로 인한 프로필 수정 실패")
    @Test
    void editProfile_invalidImageUrl_fail() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        String imageUrl = "https://programmers.co.kr/";
        String nickName = "wooriMap";
        EditProfileRequest editProfileRequest = new EditProfileRequest(imageUrl, nickName);

        // when
        MockHttpServletResponse response = 프로필_수정(accessToken, editProfileRequest);

        // then
        이미지_URL_실패(response);
    }

    private MockHttpServletResponse 프로필_수정(String accessToken, EditProfileRequest editProfileRequest) throws
        Exception {
        return mockMvc.perform(put("/api/members")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(editProfileRequest)))
            .andDo(print())
            .andReturn().getResponse();
    }

    private void 이미지_URL_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(errorResponse.getErrors().get(0).getReason()).isEqualTo("올바른 이미지 URL이 아닙니다.")
        );
    }

    private MockHttpServletResponse 회원_정보_조회(String accessToken) throws Exception {
        return mockMvc.perform(get("/api/members")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andDo(print())
            .andReturn().getResponse();
    }
}
