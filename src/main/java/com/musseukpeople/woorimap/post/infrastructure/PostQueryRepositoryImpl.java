package com.musseukpeople.woorimap.post.infrastructure;

import static com.musseukpeople.woorimap.post.domain.QPost.*;
import static com.musseukpeople.woorimap.post.domain.image.QPostImage.*;
import static com.musseukpeople.woorimap.post.domain.tag.QPostTag.*;
import static com.musseukpeople.woorimap.tag.domain.QTag.*;

import java.util.List;
import java.util.Objects;

import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.domain.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsByFilterCondition(PostFilterCondition postFilterCondition, Long coupleId) {
        List<Long> postIds = jpaQueryFactory.select(post.id)
            .from(post)
            .innerJoin(post.postTags.postTags, postTag)
            .innerJoin(postTag.tag, tag)
            .distinct()
            .where(
                coupleIdEq(coupleId),
                lastPostIdLo(postFilterCondition.getLastPostId()),
                titleContain(postFilterCondition.getTitle()),
                tagsIn(postFilterCondition.getTagIds())
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(postFilterCondition.getPaginationSize())
            .fetch();

        return jpaQueryFactory.selectFrom(post)
            .innerJoin(post.postImages.postImages, postImage)
            .fetchJoin()
            .where(
                post.id.in(postIds)
            )
            .distinct()
            .orderBy(post.id.desc())
            .fetch();
    }

    private BooleanExpression coupleIdEq(Long coupleId) {
        return post.couple.id.eq(coupleId);
    }

    private BooleanExpression lastPostIdLo(Long lastPostId) {
        return Objects.isNull(lastPostId) ? null : post.id.lt(lastPostId);
    }

    private BooleanExpression titleContain(String title) {
        return Objects.isNull(title) ? null : post.title.contains(title);
    }

    private BooleanExpression tagsIn(List<Long> tagIds) {
        return Objects.isNull(tagIds) ? null : tag.id.in(tagIds);
    }
}
