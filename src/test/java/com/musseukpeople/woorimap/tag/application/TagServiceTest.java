package com.musseukpeople.woorimap.tag.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;
import com.musseukpeople.woorimap.tag.application.dto.response.TagResponse;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.domain.TagRepository;
import com.musseukpeople.woorimap.tag.domain.Tags;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class TagServiceTest extends IntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Couple couple;

    @BeforeEach
    void setUp() {
        couple = createCouple();
    }

    @DisplayName("태그들 생성 성공")
    @Test
    void createTag_success() {
        // given
        tagRepository.saveAll(List.of(new Tag("서울", "#FFFFFF", couple)));
        List<TagRequest> tagRequests = List.of(new TagRequest("서울", "#FFFFFF"), new TagRequest("맛집", "#AAAAAA"));

        // when
        Tags tags = tagService.findOrCreateTags(couple, tagRequests);

        // then
        assertThat(tags.getList()).hasSize(2);
    }

    @DisplayName("중복된 태그 이름으로 인한 태그들 생성 실패")
    @Test
    void createTag_duplicateTagName_fail() {
        // given
        List<TagRequest> tagRequests = List.of(new TagRequest("서울", "#FFFFFF"), new TagRequest("서울", "#AAAAAA"));

        // when
        assertThatThrownBy(() -> tagService.findOrCreateTags(couple, tagRequests))
            .isInstanceOf(DuplicateTagException.class)
            .hasMessage("태그가 중복됩니다.");
    }

    @DisplayName("태그 조회 성공")
    @Test
    void getCoupleTags() {
        // given
        tagRepository.saveAll(List.of(new Tag("서울", "#FFFFFF", couple), new Tag("맛집", "#FFFFFF", couple)));

        // when
        List<TagResponse> tags = tagService.getCoupleTags(couple.getId());

        // then
        assertAll(
            () -> assertThat(tags).hasSize(2),
            () -> assertThat(tags).extracting("name").containsExactly("서울", "맛집")
        );

    }

    private Couple createCouple() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = memberRepository.saveAll(List.of(inviter, receiver));
        return coupleRepository.save(new Couple(LocalDate.now(), new CoupleMembers(members)));
    }
}
