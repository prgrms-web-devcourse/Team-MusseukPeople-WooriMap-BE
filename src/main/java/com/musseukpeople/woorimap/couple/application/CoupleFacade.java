package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.exception.CreateCoupleFailException;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleFacade {

    private final MemberService memberService;
    private final CoupleService coupleService;
    private final InviteCodeService inviteCodeService;

    @Transactional
    public void createCouple(String inviteCode, LoginMember receiver) {
        Long receiverId = receiver.getId();
        Long inviterId = inviteCodeService.getInviterIdByInviteCode(inviteCode);

        if (Objects.equals(inviterId, receiverId)) {
            throw new CreateCoupleFailException("자기 자신이 커플을 맺을 수 없습니다.", ErrorCode.NOT_CREATE_COUPLE);
        }

        Member foundInviter = memberService.getMemberById(inviterId);
        Member foundReceiver = memberService.getMemberById(receiverId);

        LocalDate createCoupleDate = LocalDate.now();
        coupleService.createCouple(foundInviter, foundReceiver, createCoupleDate);
        inviteCodeService.removeInviteCodeByCreatedCouple(inviterId, receiverId);
    }

    @Transactional
    public InviteCodeResponse createInviteCode(Long inviterId, LocalDateTime expireDate) {
        return inviteCodeService.createInviteCode(inviterId, expireDate);
    }

    @Transactional
    public void removeCouple(LoginMember member) {
        Long coupleId = member.getCoupleId();
        memberService.breakUpMembersByCoupleId(coupleId);
        coupleService.removeCouple(coupleId);
    }
}
