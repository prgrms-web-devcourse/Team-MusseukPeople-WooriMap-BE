package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.invitecode.application.InviteCodeService;
import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoupleFacade {

    private final CoupleService coupleService;
    private final InviteCodeService inviteCodeService;

    public InviteCodeResponse createInviteCode(Long inviterId, LocalDateTime expireDate) {
        return inviteCodeService.createInviteCode(inviterId, expireDate);
    }
}
