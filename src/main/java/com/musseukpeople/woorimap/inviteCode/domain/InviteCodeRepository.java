package com.musseukpeople.woorimap.inviteCode.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.inviteCode.infrastructure.InviteCodQueryRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, String>, InviteCodQueryRepository {
}
