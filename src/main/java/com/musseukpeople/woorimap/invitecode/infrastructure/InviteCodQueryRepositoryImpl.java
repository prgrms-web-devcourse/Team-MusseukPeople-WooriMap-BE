package com.musseukpeople.woorimap.invitecode.infrastructure;

import static com.musseukpeople.woorimap.invitecode.domain.QInviteCode.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.musseukpeople.woorimap.invitecode.domain.InviteCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InviteCodQueryRepositoryImpl implements InviteCodQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<InviteCode> findInviteCodeByInviterId(Long inviterId) {
        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(inviteCode)
            .where(
                inviterIdEq(inviterId),
                expireDateBeforeNow()
            )
            .fetchOne());
    }

    private BooleanExpression inviterIdEq(Long inviterId) {
        return inviteCode.inviterId.eq(inviterId);
    }

    private BooleanExpression expireDateBeforeNow() {
        return inviteCode.expireDate.after(LocalDateTime.now());
    }
}
