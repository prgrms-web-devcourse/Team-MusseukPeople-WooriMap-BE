package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CoupleRepository coupleRepository;

    @Order(1)
    @DisplayName("post 생성 성공")
    @Test
    void createPost_success() {
        // given
        // when
        Long savedPostId = createPost(createCouple(), getCreatePostRequest());

        // then
        assertThat(savedPostId).isPositive();
    }

    @Order(2)
    @DisplayName("tag 생성 성공")
    @Test
    void createTag_success() {
        // given
        // when
        List<Long> tagIdListOfThePost = createTag(createCouple(), getCreatePostRequest().getTags());

        // then
        assertThat(tagIdListOfThePost).hasSize(2);
    }

    @Order(3)
    @DisplayName("post_tag 생성 성공")
    @Test
    void createPostTag_success() {
        // given
        Long coupleId = createCouple();
        CreatePostRequest createPostRequest = getCreatePostRequest();

        Long savedPostId = createPost(coupleId, createPostRequest);
        List<Long> tagIdListOfThePost = createTag(coupleId, createPostRequest.getTags());

        // when
        postService.createPostTag(savedPostId, tagIdListOfThePost);

        // then
    }

    CreatePostRequest getCreatePostRequest() {
        List<String> sampleImagePaths = Arrays.asList("http://wooriemap.aws.com/1.jpg", "http://wooriemap.aws.com/2.jpg");
        List<TagRequest> sampleTags = Arrays.asList(new TagRequest(null, "seoul", "F000000"), new TagRequest(null, "cafe", "F000000"));

        CreatePostRequest createPostRequest = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imagePaths(sampleImagePaths)
            .tags(sampleTags)
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();

        return createPostRequest;
    }

    private Long createCouple() {
        LocalDate startDate = LocalDate.now();
        Couple couple = new Couple(startDate);
        return coupleRepository.save(couple).getId();
    }

    private Long createPost(Long coupleId, CreatePostRequest createPostRequest) {
        return postService.createPost(coupleId, createPostRequest);
    }

    private List<Long> createTag(Long coupleId, List<TagRequest> tagRequestList) {
        return tagService.createTag(coupleId, tagRequestList);
    }
}
