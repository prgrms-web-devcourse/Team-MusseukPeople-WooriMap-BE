package com.musseukpeople.woorimap.couple.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.application.dto.response.CoupleEditResponse;
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
    private static final LocalDate COUPLE_START_DATE = LocalDate.of(2022, 8, 4);

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("커플 생성 성공")
    @Test
    void create_success() {
        //given
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);

        //when
        Long coupleId = coupleService.createCouple(members, COUPLE_START_DATE);
        Member saveInviter = memberRepository.save(inviter);
        Member saveReceiver = memberRepository.save(receiver);
        Couple couple = coupleRepository.findById(coupleId).get();

        //then
        assertAll(
            () -> assertThat(saveInviter.getCouple().getId()).isEqualTo(couple.getId()),
            () -> assertThat(saveReceiver.getCouple().getId()).isEqualTo(couple.getId())
        );
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

    @DisplayName("커플 수정 성공")
    @Test
    void modify_success() {
        //given
        Couple testCouple = coupleBuilder.id(COUPLE_ID).startDate(COUPLE_START_DATE).build();
        Couple couple = coupleRepository.save(testCouple);
        LocalDate modifyDate = LocalDate.of(2022, 6, 28);
        Long coupleId = couple.getId();

        //when
        CoupleEditResponse coupleEditResponse = coupleService.modifyCouple(coupleId, modifyDate);
        Couple findCouple = coupleRepository.findById(coupleId).get();

        //then
        assertThat(coupleEditResponse.getStartDate()).isEqualTo(modifyDate);
        assertThat(findCouple.getStartDate()).isNotEqualTo(COUPLE_START_DATE);
        assertThat(findCouple.getStartDate()).isEqualTo(modifyDate);
    }

    @DisplayName("커플 조회 성공")
    @Test
    void getCouple_success() {
        //given
        Member inviter = new TMemberBuilder().email("inviter1@gmail.com").build();
        Member receiver = new TMemberBuilder().email("receiver1@gmail.com").build();
        List<Member> members = List.of(inviter, receiver);
        Long coupleId = coupleService.createCouple(members, COUPLE_START_DATE);
        memberRepository.save(inviter);
        memberRepository.save(receiver);

        //when
        Couple couple = coupleService.getCoupleWithMemberById(coupleId);

        //then
        assertAll(
            () -> assertThat(couple.getId()).isEqualTo(coupleId),
            () -> assertThat(couple.getStartDate()).isEqualTo(COUPLE_START_DATE),
            () -> assertThat(couple.getCoupleMembers().getMembers()).hasSize(2)
        );

        assertThat(couple.getCoupleMembers().getMembers()).hasSize(2);
    }

    @DisplayName("커플 삭제 성공")
    @Test
    void remove_success() {
        //given
        Couple testCouple = coupleBuilder.id(COUPLE_ID).startDate(COUPLE_START_DATE).build();
        coupleRepository.save(testCouple);
        coupleService.removeCouple(COUPLE_ID);

        //when
        Optional<Couple> foundCouple = coupleRepository.findById(COUPLE_ID);

        //then
        assertThat(foundCouple).isEmpty();
    }
}
