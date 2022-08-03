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

import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.post.entity.PostTag;
import com.musseukpeople.woorimap.post.entity.PostTagRepository;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private PostTagRepository postTagRepository;

    private static final LocalDate COUPLE_START_DATE = LocalDate.of(2022, 1, 1);

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
        Couple couple = createCouple();
        CreatePostRequest createPostRequest = getCreatePostRequest();

        Long savedPostId = createPost(couple, createPostRequest);
        List<Long> tagIdListOfThePost = createTag(couple, createPostRequest.getTags());

        // when
        postService.createPostTag(savedPostId, tagIdListOfThePost);

        // then
        List<PostTag> postTags = postTagRepository.findAll();
        assertThat(postTags).hasSize(tagIdListOfThePost.size());
    }

    private CreatePostRequest getCreatePostRequest() {
        List<String> sampleImagePaths = Arrays.asList("http://wooriemap.aws.com/1.jpg", "http://wooriemap.aws.com/2.jpg");
        List<TagRequest> sampleTags = Arrays.asList(new TagRequest("seoul", "#F000000"), new TagRequest("cafe", "F000000"));

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

    private Couple createCouple() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);
        Long coupleId = coupleService.createCouple(members, COUPLE_START_DATE);

        return coupleService.getCoupleById(coupleId);
    }

    private Long createPost(Couple couple, CreatePostRequest createPostRequest) {
        return postService.createPost(couple, createPostRequest);
    }

    private List<Long> createTag(Couple couple, List<TagRequest> tagRequestList) {
        return tagService.createTag(couple, tagRequestList);
    }
}
