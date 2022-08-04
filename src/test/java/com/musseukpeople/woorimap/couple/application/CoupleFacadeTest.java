package com.musseukpeople.woorimap.couple.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.couple.exception.NotCoupleException;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TCoupleBuilder;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class CoupleFacadeTest extends IntegrationTest {

    @Autowired
    private CoupleFacade coupleFacade;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @DisplayName("커플 확인 성공")
    @Test
    void check_success() {
        //given
        Member me = new TMemberBuilder().email("me@gamil.com").build();
        Member you = new TMemberBuilder().email("you@gmail.com").build();
        List<Member> members = List.of(me, you);
        Couple couple = new TCoupleBuilder().coupleMembers(new CoupleMembers(members)).build();

        coupleRepository.save(couple);
        Member saveMe = memberRepository.save(me);
        Member saveYou = memberRepository.save(you);

        //when
        coupleFacade.checkCouple(saveMe.getId());

        //then
        assertAll(
            () -> assertThat(saveMe.isCouple()).isTrue(),
            () -> assertThat(saveYou.isCouple()).isTrue()
        );
    }

    @DisplayName("솔로라서 커플 확인 실패")
    @Test
    void check_solo_fail() {
        //given
        Member member = new TMemberBuilder().email("me@gamil.com").build();
        Couple couple = new TCoupleBuilder().build();
        coupleRepository.save(couple);
        Long memberId = memberRepository.save(member).getId();

        //when
        assertThatThrownBy(() -> coupleFacade.checkCouple(memberId))

            //then
            .isInstanceOf(NotCoupleException.class);
    }
}
