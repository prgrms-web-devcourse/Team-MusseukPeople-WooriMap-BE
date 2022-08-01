package com.musseukpeople.woorimap.couple.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;

@SpringBootTest
class CoupleServiceTest {

    private static final Long COUPLE_ID = 1L;
    private static final LocalDate COUPLE_START_DATE = LocalDate.of(2022, 1, 1);

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private CoupleRepository coupleRepository;

    @BeforeEach
    void setup() {
        coupleRepository.save(new Couple(COUPLE_ID, COUPLE_START_DATE));
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
