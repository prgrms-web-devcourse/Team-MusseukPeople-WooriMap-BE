package com.musseukpeople.woorimap.post.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

public class PostControllerTest extends AcceptanceTest {

    private String coupleAccessToken;

    @BeforeEach
    void login() throws Exception {
        String email = "test@gmail.com";
        String password = "!Test1234";
        String nickName = "test";
        SignupRequest user = new SignupRequest(email, password, nickName);
        String accessToken = 회원가입_토큰(user);
        coupleAccessToken = 커플_맺기_토큰(accessToken);
    }

    @DisplayName("post 생성 완료")
    @Transactional
    @Test
    void create_post_success() throws Exception {
        List<String> sampleImagePaths = Arrays.asList("http://wooriemap.aws.com/1.jpg", "http://wooriemap.aws.com/2.jpg");
        List<TagRequest> sampleTags = Arrays.asList(new TagRequest("seoul", "F000000"), new TagRequest("cafe", "F000000"));

        CreatePostRequest createPostRequest = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(sampleImagePaths)
            .tags(sampleTags)
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();

        MockHttpServletResponse response = mockMvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, coupleAccessToken)
                .content(objectMapper.writeValueAsString(createPostRequest)))
            .andDo(print())
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
}
