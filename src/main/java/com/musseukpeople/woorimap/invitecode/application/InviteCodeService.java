package com.musseukpeople.woorimap.invitecode.application;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.invitecode.domain.InviteCode;
import com.musseukpeople.woorimap.invitecode.domain.InviteCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteCodeService {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private final InviteCodeRepository inviteCodeRepository;

    @Transactional
    public InviteCodeResponse createInviteCode(Long inviterId) {
        return new InviteCodeResponse(getInviteCode(inviterId).orElse(
            inviteCodeRepository.save(new InviteCode(createRandomInviterCode(), inviterId))).getCode());
    }

    private Optional<InviteCode> getInviteCode(Long inviterId) {
        return inviteCodeRepository.findInviteCodeByInviterId(inviterId);
    }

    private String createRandomInviterCode() {
        String randomInviteCodeNumber = String.valueOf(random.nextInt(10000000, 100000000));
        return randomInviteCodeNumber.substring(randomInviteCodeNumber.length() - 7);
    }
}
