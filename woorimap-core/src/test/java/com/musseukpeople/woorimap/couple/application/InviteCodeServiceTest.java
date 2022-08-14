package com.musseukpeople.woorimap.couple.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.domain.InviteCode;
import com.musseukpeople.woorimap.couple.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.couple.exception.NotFoundInviteCodeException;
import com.musseukpeople.woorimap.util.IntegrationTest;

class InviteCodeServiceTest extends IntegrationTest {

    private static final LocalDateTime EXPIRE_DATE = LocalDateTime.now().plusDays(1);
    private static final String INVITE_CODE = "1234567";
    private static final Long INVITER_ID = 1L;

    @Autowired
    private InviteCodeService inviteCodeService;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @BeforeEach
    void cleanup() {
        inviteCodeRepository.deleteAllInBatch();
    }

    @DisplayName("새로운 랜덤 코드 생성 성공")
    @Test
    void createInviteCode_success() {
        //given
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID, EXPIRE_DATE);

        //when
        InviteCode findInviteCode = inviteCodeRepository.findById(inviteCode.getCode()).get();

        //then
        assertAll(
            () -> assertThat(inviteCode.getCode()).isEqualTo(findInviteCode.getCode()),
            () -> assertThat(findInviteCode.getInviterId()).isEqualTo(INVITER_ID),
            () -> assertThat(findInviteCode.getExpireDateTime()).isAfter(LocalDateTime.now())
        );
    }

    @DisplayName("기존 코드가 존재하지만 유효기간이 만료되었다면 새로운 코드 생성 성공")
    @Test
    void createInviteCode_createNewCode_success() {
        //given
        LocalDateTime overExpireDate = LocalDateTime.now().minusHours(1);
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, overExpireDate));

        //when
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID, EXPIRE_DATE);

        //then
        assertThat(inviteCode.getCode()).isNotEqualTo(INVITE_CODE);
    }

    @DisplayName("기존 코드가 있고 만료기간이 지나지 않았다면 초대 코드 가져오기 성공")
    @Test
    void createInviteCode_getCode_success() {
        //given
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, EXPIRE_DATE));

        //when
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID, EXPIRE_DATE);

        //then
        assertThat(inviteCode.getCode()).isEqualTo(INVITE_CODE);
    }

    @DisplayName("초대 코드로 InviterId를 가지고 올 수 있다.")
    @Test
    void getInviterId_success() {
        //given
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, EXPIRE_DATE));

        //when
        Long inviterId = inviteCodeService.getIdByCode(INVITE_CODE);

        //then
        assertThat(inviterId).isEqualTo(INVITER_ID);
    }

    @DisplayName("초대 코드가 존재하지 않아 가져올 수 없다.")
    @Test
    void getInviterId_notSaved_fail() {
        //given
        String notSavedCode = "7894561";
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, EXPIRE_DATE));

        //when
        assertThatThrownBy(() -> inviteCodeService.getIdByCode(notSavedCode))

            //then
            .isInstanceOf(NotFoundInviteCodeException.class);
    }
}
