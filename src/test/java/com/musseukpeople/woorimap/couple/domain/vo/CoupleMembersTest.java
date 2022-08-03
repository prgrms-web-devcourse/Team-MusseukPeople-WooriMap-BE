package com.musseukpeople.woorimap.couple.domain.vo;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class CoupleMembersTest {

    private static final Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
    private static final Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();

    @DisplayName("커플 멤버 컬렉션 생성 성공")
    @Test
    void construct_success() {
        //given
        List<Member> members = List.of(inviter, receiver);

        //when
        CoupleMembers coupleMembers = new CoupleMembers(members);

        //then
        assertAll(
            () -> assertThat(coupleMembers.getMembers()).hasSize(2),
            () -> assertThat(coupleMembers.getMembers()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(members)
        );
    }

    @DisplayName("2명 미만의 멤버로 생성할 수 없다.")
    @Test
    void construct_underSize_2_fail() {
        //given
        List<Member> oneMemberList = List.of(CoupleMembersTest.inviter);

        //when
        assertThatThrownBy(() -> new CoupleMembers(oneMemberList))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("커플 인원이 2명이 아닙니다");
    }

    @DisplayName("3명 이상의 멤버로 생성할 수 없다.")
    @Test
    void construct_overSize_3_fail() {
        //given
        Member overMember = new TMemberBuilder().email("overSize@gamil.com").build();
        List<Member> overMembers = List.of(CoupleMembersTest.inviter, receiver, overMember);

        //when
        assertThatThrownBy(() -> new CoupleMembers(overMembers))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("커플 인원이 2명이 아닙니다");
    }
}
