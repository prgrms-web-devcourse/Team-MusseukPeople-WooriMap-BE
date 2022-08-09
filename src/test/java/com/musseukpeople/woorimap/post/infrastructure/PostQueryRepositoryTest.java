package com.musseukpeople.woorimap.post.infrastructure;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.domain.TagRepository;
import com.musseukpeople.woorimap.util.RepositoryTest;
import com.musseukpeople.woorimap.util.fixture.TCoupleBuilder;

@RepositoryTest
class PostQueryRepositoryTest {

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    private Couple couple;
    private Long coupleId;
    private List<Tag> tags;
    private List<String> postImages;

    @BeforeEach
    void setup() {
        couple = createCouple();
        tags = createTestTags(couple);
        postImages = createPostImages();

        coupleId = couple.getId();
        Post post = createTestPost(couple, tags, postImages);

        postRepository.save(post);
    }

    @DisplayName("tag 아이디로 검색 성공")
    @Test
    void findPostsByFilterCondition() {
        //given
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());
        PostFilterCondition onlyTagFilter = new PostFilterCondition(tagIds, null, null);

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(onlyTagFilter, coupleId);

        //then
        assertThat(posts).hasSize(1);

    }

    @DisplayName("게시물에 없는 tag 아이디로 검색 실패")
    @Test
    void find_notSavedTag_fail() {
        //given
        List<Long> notTagIds = List.of(-1L, -1234L, -8569L);
        PostFilterCondition onlyTagFilter = new PostFilterCondition(notTagIds, null, null);

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(onlyTagFilter, coupleId);

        //then
        assertThat(posts).isEmpty();
    }

    @DisplayName("제목으로 검색 성공")
    @ParameterizedTest
    @ValueSource(
        strings = {"테스트", "제목", "서울", "제주", "창원"}
    )
    void search_title_success(String title) {
        //given
        PostFilterCondition onlyTitleFilter = new PostFilterCondition(null, title, null);

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(onlyTitleFilter, coupleId);

        //then
        assertThat(posts).hasSize(1);
    }

    @DisplayName("제목에 없는 단어로 검색 실패")
    @ParameterizedTest
    @ValueSource(
        strings = {"test", "seoul", "jeju", "changwon"}
    )
    void search_noTitle_fail(String noTitle) {
        //given
        PostFilterCondition onlyTitleFilter = new PostFilterCondition(null, noTitle, null);

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(onlyTitleFilter, coupleId);

        //then
        assertThat(posts).isEmpty();
    }

    @DisplayName("한번에 불러오는 사이즈는 20이다.")
    @Test
    void search_limitSize_20_success() {
        //given
        for (int i = 0; i < 30; i++) {
            postRepository.save(createTestPost(couple, tags, postImages));
        }

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(
            new PostFilterCondition(null, null, null), coupleId);

        //then
        assertThat(posts).hasSize(20);
    }

    @DisplayName("모든 조건 검색 성공")
    @Test
    void search_filter_success() {
        //given
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());

        for (int i = 0; i < 30; i++) {
            postRepository.save(createTestPost(couple, tags, postImages));
        }

        //when
        List<Post> posts = postRepository.findPostsByFilterCondition(
            new PostFilterCondition(tagIds, "테스트", 54378537L), coupleId);

        //then
        assertThat(posts).hasSize(20);

    }

    private Post createTestPost(Couple couple, List<Tag> tags, List<String> postImages) {

        return Post.builder()
            .couple(couple)
            .title("테스트 제목 서울, 제주, 창원")
            .content("게시물 내용")
            .location(new Location(new BigDecimal("12.12312321"), new BigDecimal("12.12312321")))
            .imageUrls(postImages)
            .tags(tags)
            .datingDate(LocalDate.of(2022, 02, 02))
            .build();
    }

    private Couple createCouple() {
        Couple couple = coupleRepository.save(new TCoupleBuilder().build());
        return couple;
    }

    private List<String> createPostImages() {
        return List.of("http://wooriemap.aws.com/1.jpg",
            "http://wooriemap.aws.com/2.jpg",
            "http://wooriemap.aws.com/3.jpg",
            "http://wooriemap.aws.com/4.jpg",
            "http://wooriemap.aws.com/5.jpg");
    }

    private List<Tag> createTestTags(Couple couple) {
        tagRepository.saveAll(
            List.of(new Tag("서울", "#null33", couple),
                new Tag("대구", "#null33", couple),
                new Tag("대전", "#null33", couple),
                new Tag("부산", "#null33", couple),
                new Tag("창원", "#null33", couple))
        );

        return tagRepository.findAllByCoupleId(couple.getId());

    }
}
