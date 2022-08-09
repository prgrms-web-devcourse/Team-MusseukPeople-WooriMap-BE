package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.domain.TagRepository;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

@SpringBootTest
class PostServiceTest extends IntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private TagRepository tagRepository;

    private Couple couple;

    @BeforeEach
    void setUp() {
        couple = createCouple();
    }

    @DisplayName("게시물 생성 성공")
    @Test
    void createPost_success() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("cafe", "#FFFFFF", couple));
        tagRepository.saveAll(tags);
        CreatePostRequest request = createPostRequest();

        // when
        Long postId = postService.createPost(couple, tags, request);

        // then
        assertThat(postId).isPositive();
    }

    @DisplayName("중복된 태그로 인한 게시물 생성 실패")
    @Test
    void createPost_duplicateTag_fail() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("seoul", "#FFFFFF", couple));
        tagRepository.saveAll(tags);

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(couple, tags, createPostRequest()))
            .isInstanceOf(DuplicateTagException.class)
            .hasMessage("태그가 중복됩니다.");
    }

    @DisplayName("게시물 수정 성공")
    @Test
    void modifyPost_success() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("cafe", "#FFFFFF", couple));
        tagRepository.saveAll(tags);
        CreatePostRequest request = createPostRequest();
        Long postId = postService.createPost(couple, tags, request);

        List<Tag> updateTags = new ArrayList<>();
        EditPostRequest editPostRequest = editPostRequest();

        // when
        Long updatePostId = postService.modifyPost(updateTags, postId, editPostRequest);

        // then
        assertThat(postId).isEqualTo(updatePostId);
    }

    private EditPostRequest editPostRequest() {
        return EditPostRequest.builder()
            .title("2첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl3", "imageUrl4"))
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
            .datingDate(LocalDate.now())
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();
    }

    private Couple createCouple() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = memberRepository.saveAll(List.of(inviter, receiver));
        return coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
    }
}
