package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.post.exception.NotFoundPostException;
import com.musseukpeople.woorimap.post.exception.PostNotBelongToCoupleException;
import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;
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

    @Autowired
    private PostRepository postRepository;

    private LoginMember loginMember;
    private Long coupleId;

    @BeforeEach
    void setUp() {
        loginMember = createCoupleMember();
        coupleId = loginMember.getCoupleId();
    }

    @DisplayName("게시물 생성 성공")
    @Test
    void createPost_success() {
        // given
        CreatePostRequest request = createPostRequest();

        // when
        Long postId = postFacade.createPost(loginMember, request);

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
            .tags(List.of(
                new TagRequest("서울", "#FFFFFF"),
                new TagRequest("서울", "#FFFFF1"))
            )
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();

        // when
        // then
        assertThatThrownBy(() -> postFacade.createPost(loginMember, request))
            .isInstanceOf(DuplicateTagException.class)
            .hasMessage("태그가 중복됩니다.");
    }

    @DisplayName("게시물 수정 성공")
    @Transactional
    @Test
    void modifyPost_success() {
        // given
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(loginMember, request);
        EditPostRequest editRequest = editPostRequest();

        // when
        Long editPostId = postFacade.modifyPost(loginMember, postId, editRequest);

        // then
        Post post = postRepository.findById(editPostId).get();

        assertAll(
            () -> assertThat(postId).isEqualTo(editPostId),
            () -> assertThat(post.getTitle()).isEqualTo(editRequest.getTitle()),
            () -> assertThat(post.getPostTags().getPostTags()).hasSize(editRequest.getTags().size()),
            () -> assertThat(post.getPostImages().getPostImages()).hasSize(editRequest.getImageUrls().size()),
            () -> assertThat(post.getContent()).isEqualTo(editRequest.getContent()),
            () -> assertThat(post.getLocation().getLongitude()).isEqualTo(editRequest.getLongitude()),
            () -> assertThat(post.getLocation().getLatitude()).isEqualTo(editRequest.getLatitude())
        );
    }

    @DisplayName("게시물 삭제 성공")
    @Transactional
    @Test
    void deletePost_success() {
        // given
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(loginMember, request);

        // when
        postFacade.removePost(coupleId, postId);

        // then
        Optional<Post> foundPost = postRepository.findById(postId);

        assertThat(foundPost).isEmpty();
    }

    @DisplayName("게시물 삭제 실패 - 존재하지 않은 post")
    @Transactional
    @Test
    void deletePostDoesNotExistPost_fail() {
        // given
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(loginMember, request);

        // when
        // then
        assertThatThrownBy(() -> postFacade.removePost(coupleId, postId + 1))
            .isInstanceOf(NotFoundPostException.class);
    }

    @DisplayName("게시물 삭제 실패 - 다른 커플의 post 삭제")
    @Transactional
    @Test
    void deletePostNotBelongToCouple_fail() {
        // given
        CreatePostRequest request = createPostRequest();
        postFacade.createPost(loginMember, request);

        LoginMember otherCoupleMember = createOtherCoupleMember();
        Long postId2 = postFacade.createPost(otherCoupleMember, request);

        // when
        // then
        assertThatThrownBy(() -> postFacade.removePost(coupleId, postId2))
            .isInstanceOf(PostNotBelongToCoupleException.class);
    }

    private EditPostRequest editPostRequest() {
        return EditPostRequest.builder()
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
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .title("첫 이야기")
            .content("<h1>첫 이야기.... </h1>")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .datingDate(LocalDate.now())
            .tags(List.of(
                new TagRequest("서울", "#FFFFFF"),
                new TagRequest("카페", "#FFFFFF")
            ))
            .latitude(new BigDecimal("12.12312321"))
            .longitude(new BigDecimal("122.3123121"))
            .build();
    }

    private LoginMember createCoupleMember() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);
        memberRepository.saveAll(members);
        Couple couple = coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
        members.forEach(member -> member.changeCouple(couple));
        memberRepository.saveAll(members);
        return new LoginMember(receiver.getId(), couple.getId(), "accessToken");
    }

    private LoginMember createOtherCoupleMember() {
        Member inviter = new TMemberBuilder().email("inviter2@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver2@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);
        memberRepository.saveAll(members);
        Couple couple = coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
        members.forEach(member -> member.changeCouple(couple));
        memberRepository.saveAll(members);
        return new LoginMember(receiver.getId(), couple.getId(), "accessToken");
    }
}
