package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.domain.InviteCode;
import com.musseukpeople.woorimap.couple.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.couple.exception.NotFoundInviteCodeException;
import com.musseukpeople.woorimap.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteCodeService {

    private final InviteCodeRepository inviteCodeRepository;
    private final CodeGenerator codeGenerator;

    @Transactional
    public InviteCodeResponse createInviteCode(Long inviterId, LocalDateTime expireDate) {
        InviteCode inviteCode = inviteCodeRepository.findInviteCodeByInviterId(inviterId)
            .orElseGet(
                () -> inviteCodeRepository.save(new InviteCode(codeGenerator.createRandomCode(), inviterId, expireDate))
            );

        return new InviteCodeResponse(inviteCode.getCode());
    }

    public Long getIdByCode(String inviteCode) {
        InviteCode foundInviteCode = inviteCodeRepository.findById(inviteCode)
            .orElseThrow(() -> new NotFoundInviteCodeException(ErrorCode.NOT_FOUND_INVITE_CODE, inviteCode));
        return foundInviteCode.getInviterId();
    }

    public void removeInviteCodeByMembers(List<Member> members) {
        inviteCodeRepository.deleteByInIds(createMemberIds(members));
    }

    private List<Long> createMemberIds(List<Member> members) {
        List<Long> ids = new ArrayList<>();
        members.forEach(member -> ids.add(member.getId()));
        return ids;
    }
}
