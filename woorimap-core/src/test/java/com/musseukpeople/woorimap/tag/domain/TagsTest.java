package com.musseukpeople.woorimap.tag.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class TagsTest {

    private Couple couple;

    @BeforeEach
    void setUp() {
        Member member = new TMemberBuilder().build();
        Member opponent = new TMemberBuilder().email("opponent@gmail.com").build();
        couple = new Couple(LocalDate.now(), new CoupleMembers(List.of(member, opponent)));
    }

    @DisplayName("태그 컬렉션 생성 성공")
    @Test
    void create_success() {
        // given
        List<Tag> tags = List.of(new Tag("서울", "#FFFFFF", couple), new Tag("맛집", "#FFFFFF", couple));

        // when
        Tags createTags = new Tags(tags);

        // then
        assertThat(createTags).isNotNull();
    }

    @DisplayName("태그 이름이 중복될 경우 생성 실패")
    @Test
    void create_duplicateTagName_fail() {
        // given
        List<Tag> tags = List.of(new Tag("서울", "#FFFFFF", couple), new Tag("서울", "#FFFFFF", couple));

        // when
        // then
        assertThatThrownBy(() -> new Tags(tags))
            .isInstanceOf(DuplicateTagException.class)
            .hasMessage("태그가 중복됩니다.");
    }

    @DisplayName("이름 같은 태그들 제외 성공")
    @Test
    void removeAllByName_success() {
        // given
        Tag duplicateTag = new Tag("서울", "#FFFFFF", couple);
        Tags tags = new Tags(List.of(duplicateTag, new Tag("맛집", "#FFFFFF", couple)));
        Tags compareTags = new Tags(List.of(duplicateTag));

        // when
        Tags nonDuplicateTags = tags.removeAllByName(compareTags);

        // then
        assertThat(nonDuplicateTags.getList()).hasSize(1);
    }

    @DisplayName("태그들 추가 성공")
    @Test
    void addAll_success() {
        // given
        Tags tags = new Tags(List.of(new Tag("서울", "#FFFFFF", couple), new Tag("맛집", "#FFFFFF", couple)));
        Tags addTags = new Tags(List.of(new Tag("낙성대역", "#FFFFFF", couple)));

        // when
        Tags addAllTags = tags.addAll(addTags);

        // then
        assertThat(addAllTags.getList()).hasSize(3);
    }
}
