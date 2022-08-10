package com.musseukpeople.woorimap.couple.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class CoupleTest {

    private static final Member INVITER = new TMemberBuilder().email("inviter1@gmail.com").build();
    private static final Member RECEIVER = new TMemberBuilder().email("receiver1@gmail.com").build();
    private static final CoupleMembers coupleMembers = new CoupleMembers(List.of(INVITER, RECEIVER));

    @DisplayName("커플 생성 성공")
    @Test
    void construct_success() {
        //given
        LocalDate startDate = LocalDate.now();

        //when
        Couple couple = new Couple(startDate, coupleMembers);

        //then
        System.out.println(couple.getStartDate());
        assertThat(couple.getStartDate()).isEqualTo(startDate);
    }

    @DisplayName("현재 이후 날짜로 인한 생성 실패")
    @ParameterizedTest(name = "현재 날짜 + {0} 일")
    @ValueSource(
        ints = {1, 22, 333, 4444, 55555}
    )
    void couple_afterNowDate_fail(int plusDateCount) {
        //given
        LocalDate afterNowDate = LocalDate.now().plusDays(plusDateCount);

        //when
        assertThatThrownBy(() -> new Couple(afterNowDate, coupleMembers))

            //then
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 이후 날짜로 커플을 생성할 수 없습니다.");
    }
}
