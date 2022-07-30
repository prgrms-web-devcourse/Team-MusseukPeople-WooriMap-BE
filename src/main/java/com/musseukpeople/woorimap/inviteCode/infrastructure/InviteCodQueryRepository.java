package com.musseukpeople.woorimap.inviteCode.infrastructure;

import java.util.Optional;

import com.musseukpeople.woorimap.inviteCode.domain.InviteCode;

public interface InviteCodQueryRepository {

    Optional<InviteCode> findInviteCodeByInviterId(Long inviterId);
}
