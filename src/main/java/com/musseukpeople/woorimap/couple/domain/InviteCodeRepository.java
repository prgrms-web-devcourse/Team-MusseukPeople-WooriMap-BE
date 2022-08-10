package com.musseukpeople.woorimap.couple.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.couple.infrastructure.InviteCodQueryRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, String>, InviteCodQueryRepository {
}
