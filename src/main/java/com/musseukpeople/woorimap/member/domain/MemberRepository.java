package com.musseukpeople.woorimap.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailValue(String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.couple WHERE m.email.value = :email")
    Optional<Member> findMemberByEmail(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.couple = null WHERE m.couple.id = :coupleId")
    void updateCoupleIdSetNull(@Param("coupleId") Long coupleId);
}
