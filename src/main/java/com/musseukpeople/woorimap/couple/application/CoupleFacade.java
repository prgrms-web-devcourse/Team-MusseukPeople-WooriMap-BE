package com.musseukpeople.woorimap.couple.application;

import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.invitecode.application.InviteCodeService;
import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoupleFacade {

    private final InviteCodeService inviteCodeService;

    public InviteCodeResponse createInviteCode(Long inviterId) {
        return inviteCodeService.createInviteCode(inviterId);
    }
}
