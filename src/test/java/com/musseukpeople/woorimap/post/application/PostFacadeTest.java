package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.EditPostRequest;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class PostFacadeTest extends IntegrationTest {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long coupleId;

    @BeforeEach
    void setUp() {
        coupleId = createCouple().getId();
    }

    @DisplayName("게시물 생성 성공")
    @Test
    void createPost_success() {
        // given
        CreatePostRequest request = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .datingDate(LocalDate.now())
            .tags(List.of(new TagRequest("서울", "#FFFFFF"), new TagRequest("부산", "#FFFFFF")))
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();

        // when
        Long postId = postFacade.createPost(coupleId, request);

        // then
        assertThat(postId).isPositive();
    }

    @DisplayName("중복된 태그 요청으로 인한 게시물 생성 실패")
    @Test
    void createPost_duplicateTagRequest_fail() {
        // given
        CreatePostRequest request = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .tags(List.of(new TagRequest("서울", "#FFFFFF"), new TagRequest("서울", "#FFFFF1")))
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();

        // when
        // then
        assertThatThrownBy(() -> postFacade.createPost(coupleId, request))
            .isInstanceOf(DuplicateTagException.class)
            .hasMessage("태그가 중복됩니다.");
    }


    @DisplayName("게시물 수정 성공")
    @Test
    void modifyPost_success() {
        // given
        CreatePostRequest request = CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .datingDate(LocalDate.now())
            .tags(List.of(new TagRequest("서울", "#FFFFFF"), new TagRequest("부산", "#FFFFFF")))
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();
        Long postId = postFacade.createPost(coupleId, request);

        EditPostRequest editRequest = EditPostRequest.builder()
            .title("2첫 이야기")
            .content("<h1>22첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2", "imageUrl3"))
            .datingDate(LocalDate.now())
            .tags(List.of(
                new TagRequest("서울", "#FFFFFF"),
                new TagRequest("갬성", "#FFFFFF"),
                new TagRequest("카페", "#FFFFFF")
            ))
            .latitude(new BigDecimal("32.12312321"))
            .longitude(new BigDecimal("322.3123121"))
            .build();

        // when
        Long editPostId = postFacade.modifyPost(coupleId, postId, editRequest);

        // then
        assertThat(postId).isEqualTo(editPostId);
    }

    private Couple createCouple() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = memberRepository.saveAll(List.of(inviter, receiver));
        return coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
    }

}
