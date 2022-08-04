package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleCheckResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleEditResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleMemeberResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.domain.Couple;
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
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenDto createCouple(String inviteCode, LoginMember receiver) {
        Long receiverId = receiver.getId();
        Long inviterId = inviteCodeService.getIdByCode(inviteCode);

        validateSameInviter(receiverId, inviterId);

        Member foundInviter = memberService.getMemberWithCoupleById(inviterId);
        Member foundReceiver = memberService.getMemberWithCoupleById(receiverId);

        validateAlreadyCouple(foundInviter, foundReceiver);

        List<Member> members = List.of(foundInviter, foundReceiver);
        LocalDate createCoupleDate = LocalDate.now();
        Long coupleId = coupleService.createCouple(members, createCoupleDate);
        inviteCodeService.removeInviteCodeByMembers(members);

        return jwtProvider.createAccessToken(String.valueOf(receiverId), coupleId);
    }

    public CoupleResponse getCouple(LoginMember member) {
        Long coupleId = member.getCoupleId();
        Long memberId = member.getId();

        Couple couple = coupleService.getCoupleWithMemberById(coupleId);

        LocalDate startDate = couple.getStartDate();
        CoupleMemeberResponse coupleMemeberResponse = CoupleMemeberResponse.from(couple.getMyMember(memberId));
        CoupleMemeberResponse coupleYourResponse = CoupleMemeberResponse.from(couple.getOpponentMember(memberId));

        return new CoupleResponse(startDate, coupleMemeberResponse, coupleYourResponse);
    }

    @Transactional
    public CoupleEditResponse modifyCouple(Long coupleId, LocalDate modifyDate) {
        return coupleService.modifyCouple(coupleId, modifyDate);
    }

    @Transactional
    public TokenDto removeCouple(LoginMember member) {
        Long coupleId = member.getCoupleId();
        Long memberId = member.getId();

        memberService.breakUpMembersByCoupleId(coupleId);
        coupleService.removeCouple(coupleId);

        return jwtProvider.createAccessToken(String.valueOf(memberId), null);
    }

    @Transactional
    public InviteCodeResponse createInviteCode(Long inviterId, LocalDateTime expireDate) {
        return inviteCodeService.createInviteCode(inviterId, expireDate);
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

    public CoupleCheckResponse checkCouple(Long loginMemberId) {
        Member member = memberService.getMemberWithCoupleById(loginMemberId);
        String memberId = String.valueOf(member.getId());

        boolean isCouple = member.isCouple();
        String accessToken = isCouple ?
            jwtProvider.createAccessToken(memberId, member.getCouple().getId()).getValue() : null;

        return new CoupleCheckResponse(accessToken, isCouple);
    }
}
