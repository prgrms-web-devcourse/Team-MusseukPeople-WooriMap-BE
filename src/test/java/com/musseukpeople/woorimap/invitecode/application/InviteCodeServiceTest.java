package com.musseukpeople.woorimap.invitecode.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.invitecode.domain.InviteCode;
import com.musseukpeople.woorimap.invitecode.domain.InviteCodeRepository;

@SpringBootTest
class InviteCodeServiceTest {

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
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID);

        //when
        InviteCode findInviteCode = inviteCodeRepository.findById(inviteCode.getCode()).get();

        //then
        assertAll(
            () -> assertThat(inviteCode.getCode()).isEqualTo(findInviteCode.getCode()),
            () -> assertThat(findInviteCode.getInviterId()).isEqualTo(INVITER_ID),
            () -> assertThat(findInviteCode.getExpireDate()).isAfter(LocalDateTime.now())
        );
    }

    @DisplayName("기존 코드가 존재하지만 유효기간이 만료되었다면 새로운 코드 생성 성공")
    @Test
    void createInviteCode_createNewCode_success() {
        //given
        LocalDateTime overExpireDate = LocalDateTime.now().minusHours(1);
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID, overExpireDate));

        //when
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID);

        //then
        assertThat(inviteCode.getCode()).isNotEqualTo(INVITE_CODE);
    }

    @DisplayName("기존 코드가 있고 만료기간이 지나지 않았다면 초대 코드 가져오기 성공")
    @Test
    void createInviteCode_getCode_success() {
        //given
        inviteCodeRepository.save(new InviteCode(INVITE_CODE, INVITER_ID));

        //when
        InviteCodeResponse inviteCode = inviteCodeService.createInviteCode(INVITER_ID);

        //then
        assertThat(inviteCode.getCode()).isEqualTo(INVITE_CODE);
    }
}
