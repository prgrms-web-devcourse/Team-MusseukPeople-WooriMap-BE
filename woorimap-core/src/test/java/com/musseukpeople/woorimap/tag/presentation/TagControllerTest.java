package com.musseukpeople.woorimap.tag.presentation;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;
import com.musseukpeople.woorimap.tag.application.dto.response.TagResponse;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class TagControllerTest extends AcceptanceTest {

    @DisplayName("태그 조회 성공")
    @Test
    void showAll() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Hwan1234", "Hwan"));
        accessToken = 커플_맺기_토큰(accessToken);
        게시글_작성(accessToken, createPostRequestWithTag("서울", "부산", "맛집"));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/couples/tags")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn().getResponse();

        // then
        List<TagResponse> tagResponses = getResponseList(response, TagResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(tagResponses).hasSize(3)
        );
    }

    private CreatePostRequest createPostRequestWithTag(String... names) {
        List<TagRequest> tagRequests = Arrays.stream(names)
            .map(name -> new TagRequest(name, "#FFFFFF"))
            .collect(toList());

        return CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .tags(tagRequests)
            .datingDate(LocalDate.now())
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();
    }
}
