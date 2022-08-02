package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.domain.InviteCode;
import com.musseukpeople.woorimap.couple.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.couple.exception.NotFoundInviteCodeException;

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

    public Long getInviterIdByInviteCode(String inviteCode) {
        InviteCode foundInviteCode = inviteCodeRepository.findById(inviteCode)
            .orElseThrow(() -> new NotFoundInviteCodeException(ErrorCode.NOT_FOUND_INVITE_CODE, inviteCode));
        return foundInviteCode.getInviterId();
    }

    public void removeInviteCodeByCreatedCouple(Long inviterId, Long receiverId) {
        inviteCodeRepository.deleteByTwoMemberId(inviterId, receiverId);
    }
}
