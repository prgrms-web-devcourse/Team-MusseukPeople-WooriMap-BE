package com.musseukpeople.woorimap.invitecode.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.invitecode.infrastructure.InviteCodQueryRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, String>, InviteCodQueryRepository {
}
