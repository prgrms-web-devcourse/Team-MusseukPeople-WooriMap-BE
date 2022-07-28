package com.musseukpeople.woorimap.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailValue(String email);

    Optional<Member> findMemberByEmailValue(String email);
}
