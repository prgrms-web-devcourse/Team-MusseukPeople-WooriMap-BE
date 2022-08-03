package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.exception.AlreadyCoupleException;
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

        validateSameInviter(receiverId, inviterId);

        Member foundInviter = memberService.getMemberById(inviterId);
        Member foundReceiver = memberService.getMemberById(receiverId);

        validateAlreadyCouple(foundInviter, foundReceiver);

        List<Member> members = List.of(foundInviter, foundReceiver);
        LocalDate createCoupleDate = LocalDate.now();
        coupleService.createCouple(members, createCoupleDate);
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

    private void validateAlreadyCouple(Member foundInviter, Member foundReceiver) {
        if (foundInviter.isCouple() || foundReceiver.isCouple()) {
            throw new AlreadyCoupleException(ErrorCode.ALREADY_COUPLE);
        }
    }

    private void validateSameInviter(Long receiverId, Long inviterId) {
        if (Objects.equals(inviterId, receiverId)) {
            throw new CreateCoupleFailException("자기 자신이 커플을 맺을 수 없습니다.", ErrorCode.NOT_CREATE_COUPLE);
        }
    }
}
