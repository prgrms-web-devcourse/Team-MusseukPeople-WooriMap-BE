package com.musseukpeople.woorimap.couple.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TCoupleBuilder;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class CoupleServiceTest extends IntegrationTest {

    private static final TCoupleBuilder coupleBuilder = new TCoupleBuilder();
    private static final Long COUPLE_ID = 1L;
    private static final LocalDate COUPLE_START_DATE = LocalDate.of(2022, 1, 1);

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Couple testCouple = coupleBuilder.id(COUPLE_ID).startDate(COUPLE_START_DATE).build();
        coupleRepository.save(testCouple);
    }

    @DisplayName("커플 생성 성공")
    @Test
    void create_success() {
        //given
        Member inviter = memberRepository.save(new TMemberBuilder().email("inviter1@gmail.com").build());
        Member receiver = memberRepository.save(new TMemberBuilder().email("receiver1@gmail.com").build());
        List<Member> members = List.of(inviter, receiver);

        //when
        Long coupleId = coupleService.createCouple(members, COUPLE_START_DATE);
        Optional<Couple> couple = coupleRepository.findById(coupleId);

        //then
        assertThat(couple).isPresent();
    }

    @DisplayName("멤버 1명으로 커플 생성 실패")
    @Test
    void create_badMembers_fail() {
        //given
        Member inviter = memberRepository.save(new TMemberBuilder().email("inviter1@gmail.com").build());
        List<Member> members = List.of(inviter);

        //when
        assertThatThrownBy(() -> coupleService.createCouple(members, COUPLE_START_DATE))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("커플 인원이 2명이 아닙니다");
    }

    @DisplayName("커플 조회 성공")
    @Test
    void getCouple_success() {
        //given
        //when
        Couple couple = coupleService.getCoupleByIdFetchMember(COUPLE_ID);

        //then
        assertAll(
            () -> assertThat(couple.getId()).isEqualTo(COUPLE_ID),
            () -> assertThat(couple.getCoupleMembers().getMembers()).hasSize(2)
        );
    }

    @DisplayName("커플 삭제 성공")
    @Test
    void remove_success() {
        //given
        coupleService.removeCouple(COUPLE_ID);

        //when
        Optional<Couple> foundCouple = coupleRepository.findById(COUPLE_ID);

        //then
        assertThat(foundCouple).isEmpty();
    }
}
