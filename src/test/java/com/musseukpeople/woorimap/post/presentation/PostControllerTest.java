package com.musseukpeople.woorimap.post.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class PostControllerTest extends AcceptanceTest {

    private String coupleAccessToken;

    @BeforeEach
    void login() throws Exception {
        String email = "test@gmail.com";
        String password = "!Test1234";
        String nickName = "test";
        String accessToken = 회원가입_토큰(new SignupRequest(email, password, nickName));
        coupleAccessToken = 커플_맺기_토큰(accessToken);
    }

    @DisplayName("post 생성 완료")
    @Test
    void create_post_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();

        // when
        MockHttpServletResponse response = 게시글_작성(coupleAccessToken, createRequest);

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull()
        );
    }

    @DisplayName("post 수정 완료")
    @Test
    void modify_post_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();
        게시글_작성(coupleAccessToken, createRequest);

        CreatePostRequest editRequest = editPostRequest();

        // when
        MockHttpServletResponse response = 게시글_수정(coupleAccessToken, editRequest, 1L);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private CreatePostRequest editPostRequest() {
        return CreatePostRequest.builder()
            .title("2첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl3", "imageUrl4"))
            .tags(List.of(
                new TagRequest("서울", "#FFFFFF"),
                new TagRequest("갬성", "#FFFFFF"),
                new TagRequest("카페", "#FFFFFF")
            ))
            .datingDate(LocalDate.now())
            .latitude(new BigDecimal("13.12312321"))
            .longitude(new BigDecimal("123.3123121"))
            .build();
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .tags(List.of(
                new TagRequest("서울", "#FFFFFF"),
                new TagRequest("갬성", "#FFFFFF"),
                new TagRequest("카페", "#FFFFFF")
            ))
            .datingDate(LocalDate.now())
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();
    }
}
