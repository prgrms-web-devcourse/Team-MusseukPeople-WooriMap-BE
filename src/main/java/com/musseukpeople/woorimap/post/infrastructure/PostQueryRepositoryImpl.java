package com.musseukpeople.woorimap.post.infrastructure;

import static com.musseukpeople.woorimap.post.domain.QPost.*;
import static com.musseukpeople.woorimap.post.domain.image.QPostImage.*;

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

        return jpaQueryFactory.select(post)
            .from(post)
            .innerJoin(post.postImages.postImages, postImage)
            .fetchJoin()
            .where(
                lastPostIdLo(postFilterCondition.getLastPostId()),
                titleContain(postFilterCondition.getTitle()),
                tagsIn(postFilterCondition.getTagIds())
            )
            .orderBy(post.id.desc())
            .limit(postFilterCondition.getPaginationSize())
            .distinct()
            .fetch();
    }

    private BooleanExpression lastPostIdLo(Long lastPostId) {
        return Objects.isNull(lastPostId) ? null : post.id.lt(lastPostId);
    }

    private BooleanExpression titleContain(String title) {
        return Objects.isNull(title) ? null : post.title.contains(title);
    }

    private BooleanExpression tagsIn(List<Long> tagIds) {
        return Objects.isNull(tagIds) ? null : post.postTags.postTags.any().tag.id.in(tagIds);
    }
}
