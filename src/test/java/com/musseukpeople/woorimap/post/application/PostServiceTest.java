package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

@SpringBootTest
class PostServiceTest extends IntegrationTest {

    private static final LocalDate COUPLE_START_DATE = LocalDate.of(2022, 1, 1);

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("post 생성 성공 - postFacade")
    @Test
    void createPost_success() {
        // given
        CreatePostRequest createPostRequest = getCreatePostRequest();

        // when
        Long postId = postFacade.createPost(createCouple().getId(), createPostRequest);

        // then
        assertThat(postId).isPositive();
    }

    @DisplayName("tag 생성 성공")
    @Test
    void createTag_success() {
        // given
        CreatePostRequest createPostRequest = getCreatePostRequest();
        Couple couple = createCouple();

        // when
        List<Tag> tagIdListOfThePost = createTag(couple, createPostRequest.getTags());

        // then
        assertThat(tagIdListOfThePost).hasSize(2);
    }

    @DisplayName("post 생성 성공")
    @Transactional
    @Test
    void createPostTag_success() {
        // given
        Couple couple = createCouple();
        CreatePostRequest createPostRequest = getCreatePostRequest();

        List<Tag> tagOfPost = createTag(couple, createPostRequest.getTags());
        Long savedPostId = createPost(couple, createPostRequest, tagOfPost);

        // when
        // then
        Post postFromDb = postRepository.findById(savedPostId).get();

        assertAll(
            () -> assertThat(postFromDb.getPostTags()).hasSize(tagOfPost.size()),
            () -> assertThat(postFromDb.getPostTags().get(0).getPost().getId()).isEqualTo(savedPostId),
            () -> assertThat(postFromDb.getPostImages()).hasSize(createPostRequest.getImageUrls().size()),
            () -> assertThat(postFromDb.getPostImages().get(0).getPost().getId()).isEqualTo(savedPostId),
            () -> assertThat(postFromDb.getLocation().getLongitude()).isEqualTo(createPostRequest.getLongitude()),
            () -> assertThat(postFromDb.getLocation().getLatitude()).isEqualTo(createPostRequest.getLatitude())
        );
    }

    private CreatePostRequest getCreatePostRequest() {
        List<String> sampleImagePaths = List.of("http://wooriemap.aws.com/1.jpg", "http://wooriemap.aws.com/2.jpg");
        List<TagRequest> sampleTags = List.of(new TagRequest("seoul", "#F000000"), new TagRequest("cafe", "F000000"));

        CreatePostRequest createPostRequest = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(sampleImagePaths)
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

    private Long createPost(Couple couple, CreatePostRequest createPostRequest, List<Tag> tagsOfPost) {
        return postService.createPost(couple, createPostRequest, tagsOfPost);
    }

    private List<Tag> createTag(Couple couple, List<TagRequest> tagRequestList) {
        return tagService.createTag(couple, tagRequestList);
    }
}
