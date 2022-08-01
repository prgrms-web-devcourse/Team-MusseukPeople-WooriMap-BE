package com.musseukpeople.woorimap.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.common.model.ApiResponse;
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

    protected String 로그인_토큰(SignInRequest signInRequest) throws Exception {
        MockHttpServletResponse response = 로그인(signInRequest);
        return "Bearer" + getResponseObject(response, TokenResponse.class).getAccessToken();
    }

    protected ErrorResponse getErrorResponse(MockHttpServletResponse response) throws IOException {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
    }

    protected <T> T getResponseObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, type);
        ApiResponse<T> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }
}
