package com.musseukpeople.woorimap.member.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.util.RepositoryTest;
import com.musseukpeople.woorimap.util.fixture.TCoupleBuilder;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @DisplayName("커플일 시 커플 멤버 조회 성공")
    @Test
    void findOpponentMember_success() {
        // given
        Member member = new TMemberBuilder().build();
        Member opponentMember = new TMemberBuilder().email("opponentMember@gmail.com").build();
        Long id = memberRepository.save(member).getId();
        memberRepository.save(opponentMember);

        Couple couple = new TCoupleBuilder().coupleMembers(new CoupleMembers(List.of(member, opponentMember))).build();
        Long coupleId = coupleRepository.save(couple).getId();

        // when
        Member findOpponentMember = memberRepository.findOpponentMember(id, coupleId).get();

        // then
        assertThat(findOpponentMember).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(opponentMember);
    }
}
