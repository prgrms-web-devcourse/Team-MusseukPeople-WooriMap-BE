package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.member.application.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleFacade {

    private final MemberService memberService;
    private final CoupleService coupleService;
    private final InviteCodeService inviteCodeService;

    @Transactional
    public InviteCodeResponse createInviteCode(Long inviterId, LocalDateTime expireDate) {
        return inviteCodeService.createInviteCode(inviterId, expireDate);
    }

    @Transactional
    public void removeCouple(Long coupleId) {
        memberService.breakUpMembersByCoupleId(coupleId);
        coupleService.removeCouple(coupleId);
    }
}
