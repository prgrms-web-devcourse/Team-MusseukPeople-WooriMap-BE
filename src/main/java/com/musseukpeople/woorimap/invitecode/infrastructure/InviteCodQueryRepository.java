package com.musseukpeople.woorimap.invitecode.infrastructure;

import java.util.Optional;

import com.musseukpeople.woorimap.invitecode.domain.InviteCode;

public interface InviteCodQueryRepository {

    Optional<InviteCode> findInviteCodeByInviterId(Long inviterId);
}
