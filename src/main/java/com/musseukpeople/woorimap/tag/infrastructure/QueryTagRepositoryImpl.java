package com.musseukpeople.woorimap.tag.infrastructure;

import static com.musseukpeople.woorimap.tag.entity.QTag.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.entity.Tag;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryTagRepositoryImpl implements QueryTagRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tag> findExistTagByCoupleId(Long coupleId, List<TagRequest> tagRequestList) {
        return jpaQueryFactory.selectFrom(tag)
            .where(tag.couple.eq(new Couple(coupleId)), tag.name.in(getTagNameInTagRequestList(tagRequestList)))
            .fetch();
    }

    public List<String> getTagNameInTagRequestList(List<TagRequest> tagRequestList) {
        List<String> tagNameList = new ArrayList<>();
        for (TagRequest tr : tagRequestList) {
            tagNameList.add(tr.getName());
        }
        return tagNameList;
    }
}
