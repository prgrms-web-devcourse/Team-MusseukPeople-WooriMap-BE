package com.musseukpeople.woorimap.couple.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.InviteCode;
import com.musseukpeople.woorimap.couple.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.util.RepositoryTest;

@RepositoryTest
class InviteCodQueryRepositoryTest {

    private static final LocalDateTime EXPIRE_DATE = LocalDateTime.now().plusDays(1);
    private static final String INVITE_CODE = "1234567";
    private static final Long INVITER_ID = 1L;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @BeforeEach
    void before() {
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, EXPIRE_DATE));
    }

    @DisplayName("초대자 아이디로 초대링크 검색 성공")
    @Test
    void findInviteCodeByInviterId_success() {
        //given
        Optional<InviteCode> findCode = inviteCodeRepository.findInviteCodeByInviterId(INVITER_ID);

        //when
        InviteCode inviteCode = findCode.orElse(null);

        //then
        assertAll(
            () -> assertThat(inviteCode).isNotNull(),
            () -> assertThat(inviteCode.getCode()).isEqualTo(INVITE_CODE),
            () -> assertThat(inviteCode.getExpireDateTime()).isAfter(LocalDateTime.now()),
            () -> assertThat(inviteCode.getExpireDateTime()).isBeforeOrEqualTo(LocalDateTime.now().plusDays(1))
        );
    }

    @DisplayName("만료 기한 넘은 초대 코드 조회 실패")
    @Test
    void findInviteCodeByInviterId_overExpireDate_fail() {
        //given
        InviteCode firstSavedCode = inviteCodeRepository.findInviteCodeByInviterId(INVITER_ID).get();
        LocalDateTime overExpireDate = LocalDateTime.now().minusDays(1);
        inviteCodeRepository.delete(firstSavedCode);
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, overExpireDate));

        //when
        Optional<InviteCode> findInviteCode = inviteCodeRepository.findInviteCodeByInviterId(INVITER_ID);

        //then
        assertThat(findInviteCode).isEmpty();
    }
}
