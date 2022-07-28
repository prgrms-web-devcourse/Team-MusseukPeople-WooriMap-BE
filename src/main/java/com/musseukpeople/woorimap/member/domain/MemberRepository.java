package com.musseukpeople.woorimap.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.member.domain.vo.Email;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    Optional<Member> findMemberByEmail(Email email);
}
