package com.musseukpeople.woorimap.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.presentation.dto.response.LoginResponse;
import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.couple.application.dto.request.CreateCoupleRequest;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AcceptanceTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

    protected MockHttpServletResponse 회원가입(SignupRequest signupRequest) throws Exception {
        return mockMvc.perform(post("/api/members/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected String 회원가입_토큰(SignupRequest signupRequest) throws Exception {
        회원가입(signupRequest);
        return 로그인_토큰(new SignInRequest(signupRequest.getEmail(), signupRequest.getPassword()));
    }

    protected MockHttpServletResponse 로그인(SignInRequest signInRequest) throws Exception {
        return mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signInRequest)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected void 커플_맺기(String accessToken) throws Exception {
        String rEmail = "receiver@gmail.com";
        String rPassword = "!Recevier123";
        String rNickName = "receiver";
        String inviteCode = createInviteCodeApi(accessToken).getContentAsString().replaceAll("[^0-9]", "");
        회원가입(new SignupRequest(rEmail, rPassword, rNickName));
        String receiverToken = 로그인_토큰(new SignInRequest(rEmail, rPassword));
        CreateCoupleRequest createCoupleRequest = new CreateCoupleRequest(inviteCode);
        
        mockMvc.perform(post("/api/couples")
            .header(HttpHeaders.AUTHORIZATION, receiverToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createCoupleRequest)));
    }

    protected String 로그인_토큰(SignInRequest signInRequest) throws Exception {
        MockHttpServletResponse response = 로그인(signInRequest);
        return "Bearer" + getResponseObject(response, LoginResponse.class).getAccessToken();
    }

    protected ErrorResponse getErrorResponse(MockHttpServletResponse response) throws IOException {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
    }

    protected <T> T getResponseObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, type);
        ApiResponse<T> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }

    private MockHttpServletResponse createInviteCodeApi(String accessToken) throws Exception {
        return mockMvc.perform(post("/api/couples/invite")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn().getResponse();
    }
}
