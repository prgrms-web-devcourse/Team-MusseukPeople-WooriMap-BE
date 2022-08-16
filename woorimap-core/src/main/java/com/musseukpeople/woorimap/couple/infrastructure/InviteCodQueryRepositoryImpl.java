package com.musseukpeople.woorimap.couple.infrastructure;

import static com.musseukpeople.woorimap.couple.domain.QInviteCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.musseukpeople.woorimap.couple.domain.InviteCode;
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
                expireDateTimeBeforeNow()
            )
            .fetchOne());
    }

    @Override
    public void deleteByInIds(List<Long> ids) {
        jpaQueryFactory.delete(inviteCode)
            .where(inviteCode.inviterId.in(ids));
    }

    private BooleanExpression inviterIdEq(Long inviterId) {
        return inviteCode.inviterId.eq(inviterId);
    }

    private BooleanExpression expireDateTimeBeforeNow() {
        return inviteCode.expireDateTime.after(LocalDateTime.now());
    }
}
