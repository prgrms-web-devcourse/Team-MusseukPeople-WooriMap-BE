package com.musseukpeople.woorimap.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import com.musseukpeople.woorimap.notification.infrastructure.EmitterRepository;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;

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
    private EmitterRepository emitterRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
        emitterRepository.deleteAllStartWithByMemberId("");
        emitterRepository.deleteAllEventCacheStartWithByMemberId("");
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
        return "Bearer" + getResponseObject(response, LoginResponse.class).getAccessToken();
    }

    protected MockHttpServletResponse 커플_맺기(String accessToken) throws Exception {
        String iEmail = "inviter@gmail.com";
        String iPassword = "!Inviter123";
        String iNickName = "inviter";
        회원가입(new SignupRequest(iEmail, iPassword, iNickName));
        String inviterToken = 로그인_토큰(new SignInRequest(iEmail, iPassword));
        String inviteCode = createInviteCodeApi(inviterToken).getContentAsString().replaceAll("[^0-9]", "");
        CreateCoupleRequest createCoupleRequest = new CreateCoupleRequest(inviteCode);

        return mockMvc.perform(post("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCoupleRequest)))
            .andReturn().getResponse();
    }

    protected String 커플_맺기_토큰(String accessToken) throws Exception {
        MockHttpServletResponse response = 커플_맺기(accessToken);
        return "Bearer" + getResponseObject(response, LoginResponse.class).getAccessToken();
    }

    protected MockHttpServletResponse 게시글_작성(String token, CreatePostRequest request) throws Exception {
        return mockMvc.perform(post("/api/couples/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected MockHttpServletResponse 게시글_수정(String token, CreatePostRequest request, Long postId) throws Exception {
        return mockMvc.perform(put("/api/couples/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected MockHttpServletResponse 게시글_삭제(String token, Long postId) throws Exception {
        return mockMvc.perform(delete("/api/couples/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected ErrorResponse getErrorResponse(MockHttpServletResponse response) throws IOException {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
    }

    protected <T> T getResponseObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, type);
        ApiResponse<T> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }

    protected <T> List<T> getResponseList(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType insideType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, insideType);
        ApiResponse<List<T>> result = objectMapper.readValue(response.getContentAsString(), javaType);
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
