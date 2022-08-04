package com.musseukpeople.woorimap.couple.infrastructure;

import java.util.Optional;

import com.musseukpeople.woorimap.couple.domain.Couple;

public interface CoupleQueryRepository {

    Optional<Couple> findWithMemberById(Long coupleId);
}
