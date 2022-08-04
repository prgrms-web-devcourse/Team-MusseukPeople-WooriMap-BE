package com.musseukpeople.woorimap.couple.infrastructure;

import static com.musseukpeople.woorimap.couple.domain.QCouple.*;
import static com.musseukpeople.woorimap.member.domain.QMember.*;

import java.util.Optional;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoupleQueryRepositoryImpl implements CoupleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Couple> findWithMemberById(Long coupleId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(couple)
            .leftJoin(couple.coupleMembers.members, member)
            .fetchJoin()
            .where(coupleIdEq(coupleId))
            .fetchOne());
    }

    private BooleanExpression coupleIdEq(Long coupleId) {
        return couple.id.eq(coupleId);
    }
}
