package com.musseukpeople.woorimap.post.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.application.dto.response.PostSearchResponse;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
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
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EntityManager entityManager;

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
        Long postId = postService.createPost(couple, tags, request).getId();

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

    @DisplayName("검색 성공")
    @Test
    void search_success() {
        //given
        Long coupleId = couple.getId();

        tagRepository.saveAll(
            List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("changwon", "#FFFFFF", couple)));
        List<Tag> tags = tagRepository.findAllByCoupleId(coupleId);

        CreatePostRequest postRequest = createPostRequest();
        postService.createPost(couple, tags, postRequest);
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());
        PostFilterCondition filterCondition = new PostFilterCondition(tagIds, "이야기", null);

        //when
        List<PostSearchResponse> searchPosts = postService.searchPosts(filterCondition, coupleId);

        //then
        assertThat(searchPosts).hasSize(1);
    }

    @DisplayName("필터 없을 시 전체 조회")
    @Test
    void search_noFilter_success() {
        //given
        tagRepository.saveAll(
            List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("changwon", "#FFFFFF", couple)));
        Long coupleId = couple.getId();
        List<Tag> tags = tagRepository.findAllByCoupleId(coupleId);

        CreatePostRequest postRequest = createPostRequest();
        postService.createPost(couple, tags, postRequest);
        PostFilterCondition filterCondition = new PostFilterCondition(null, null, null);

        //when
        List<PostSearchResponse> searchPosts = postService.searchPosts(filterCondition, coupleId);

        //then
        assertThat(searchPosts).hasSize(1);
    }

    @DisplayName("게시물 수정 성공")
    @Test
    void modifyPost_success() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("cafe", "#FFFFFF", couple));
        tagRepository.saveAll(tags);
        CreatePostRequest request = createPostRequest();
        Long postId = postService.createPost(couple, tags, request).getId();

        List<Tag> updateTags = new ArrayList<>();
        EditPostRequest editPostRequest = editPostRequest();

        // when
        Long updatePostId = postService.modifyPost(updateTags, postId, editPostRequest).getId();

        // then
        assertThat(postId).isEqualTo(updatePostId);
    }

    @DisplayName("게시글 단건 조회 성공")
    @Transactional
    @Test
    void getPostWithFetchById_success() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("cafe", "#FFFFFF", couple));
        tagRepository.saveAll(tags);
        CreatePostRequest request = createPostRequest();
        Long postId = postService.createPost(couple, tags, request).getId();
        entityManager.clear();

        // when
        Post post = postService.getPostWithFetchById(postId);

        // then
        assertThat(post.getImageUrls()).isNotNull();
    }

    @DisplayName("게시물 삭제 성공")
    @Test
    void deletePost_success() {
        // given
        List<Tag> tags = List.of(new Tag("seoul", "#FFFFFF", couple), new Tag("cafe", "#FFFFFF", couple));
        tagRepository.saveAll(tags);
        CreatePostRequest request = createPostRequest();
        Long postId = postService.createPost(couple, tags, request).getId();

        // when
        postService.removePost(postId);

        //then
        Optional<Post> foundPost = postRepository.findById(postId);

        assertThat(foundPost).isEmpty();
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
