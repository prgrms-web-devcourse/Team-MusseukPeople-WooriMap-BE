package com.musseukpeople.woorimap.couple.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.couple.infrastructure.CoupleQueryRepository;

public interface CoupleRepository extends JpaRepository<Couple, Long>, CoupleQueryRepository {
}
