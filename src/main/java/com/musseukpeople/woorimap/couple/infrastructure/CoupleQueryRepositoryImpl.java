package com.musseukpeople.woorimap.couple.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoupleQueryRepositoryImpl implements CoupleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
