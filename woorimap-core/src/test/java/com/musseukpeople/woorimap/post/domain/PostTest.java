package com.musseukpeople.woorimap.post.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class PostTest {

    @DisplayName("post 생성 실패 - tag 0개")
    @Test
    void createPost_fail() {
        // given
        Couple couple = createCouple();
        List<Tag> tags = List.of();
        List<String> imageUrls = List.of("imageUrl1", "imageUrl2", "imageUrl3");

        // when
        // then
        assertThatThrownBy(() -> getPost(imageUrls, tags, couple))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("태그는 1개 이상 등록해야합니다");
    }

    @DisplayName("post 생성 실패 - imageUrl 0개")
    @Test
    void createPost_fail2() {
        // given
        Couple couple = createCouple();
        List<Tag> tags = List.of(
            new Tag("cafe", "#F00000", couple),
            new Tag("cafe2", "#F00000", couple)
        );
        List<String> imageUrls = List.of();

        // when
        // then
        assertThatThrownBy(() -> getPost(imageUrls, tags, couple))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미지는 1개 이상 등록해야합니다");
    }

    @DisplayName("post 생성 실패 - imageUrl 5개 이상")
    @Test
    void createPost_fail3() {
        // given
        Couple couple = createCouple();
        List<Tag> tags = List.of(
            new Tag("cafe", "#F00000", couple),
            new Tag("cafe2", "#F00000", couple)
        );
        List<String> imageUrls = List.of(
            "imageUrl1",
            "imageUrl2",
            "imageUrl3",
            "imageUrl4",
            "imageUrl5",
            "imageUrl6"
        );

        // when
        // then
        assertThatThrownBy(() -> getPost(imageUrls, tags, couple))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미지는 최대 5개 등록 가능합니다");
    }

    private Post getPost(List<String> imageUrls, List<Tag> tags,Couple couple) {
        return Post.builder()
            .title("hi")
            .imageUrls(imageUrls)
            .datingDate(LocalDate.now())
            .tags(tags)
            .couple(couple)
            .build();
    }

    private Couple createCouple() {
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);
        return new Couple(LocalDate.now(), new CoupleMembers(members));
    }
}
