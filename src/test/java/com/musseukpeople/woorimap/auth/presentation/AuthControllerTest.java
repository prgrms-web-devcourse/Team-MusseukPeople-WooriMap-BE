package com.musseukpeople.woorimap.auth.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends AcceptanceTest {

    @DisplayName("로그인 성공")
    @Test
    void signIn_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        String nickName = "hwan";
        회원가입(new SignupRequest(email, password, nickName));
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signInRequest)))
            .andDo(print())
            .andReturn().getResponse();

        // then
        TokenResponse tokenResponse = getResponseObject(response, TokenResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(tokenResponse.getAccessToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getRefreshToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getMember().getEmail()).isEqualTo(email),
            () -> assertThat(tokenResponse.getMember().getNickName()).isEqualTo(nickName)
        );
    }
}
