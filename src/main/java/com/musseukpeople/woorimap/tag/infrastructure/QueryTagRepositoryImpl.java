package com.musseukpeople.woorimap.tag.infrastructure;

import static com.musseukpeople.woorimap.tag.domain.QTag.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryTagRepositoryImpl implements QueryTagRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tag> findExistTagByCouple(Couple couple, List<String> tagNames) {
        return jpaQueryFactory.selectFrom(tag)
            .where(
                tag.couple.eq(couple),
                tag.name.value.in(tagNames)
            )
            .fetch();
    }
}
