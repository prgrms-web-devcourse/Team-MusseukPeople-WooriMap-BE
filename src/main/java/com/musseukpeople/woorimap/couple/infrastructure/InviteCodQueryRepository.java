package com.musseukpeople.woorimap.couple.infrastructure;

import java.util.List;
import java.util.Optional;

import com.musseukpeople.woorimap.couple.domain.InviteCode;

public interface InviteCodQueryRepository {

    Optional<InviteCode> findInviteCodeByInviterId(Long inviterId);

    void deleteByInIds(List<Long> ids);
}
