package com.musseukpeople.woorimap.post.infrastructure;

import static com.musseukpeople.woorimap.post.domain.QPost.*;
import static com.musseukpeople.woorimap.post.domain.image.QPostImage.*;
import static com.musseukpeople.woorimap.post.domain.tag.QPostTag.*;
import static com.musseukpeople.woorimap.tag.domain.QTag.*;

import java.util.List;
import java.util.Objects;

import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.application.dto.response.PostSearchResponse;
import com.musseukpeople.woorimap.post.domain.image.QPostImages;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostSearchResponse> findPostsByFilterCondition(PostFilterCondition postFilterCondition, Long coupleId) {
        // List<PostSearchResponse> responses = jpaQueryFactory.select(
        //         Projections.constructor(PostSearchResponse.class,
        //             post.id.as("postId"),
        //             postImage.imageUrl.as("imageUrl"),
        //             post.title.as("title"),
        //             post.createdDateTime.as("createDateTime"),
        //             post.location.latitude.as("latitude"),
        //             post.location.longitude.as("longitude")
        //         )
        //     )
        //     .distinct()
        //     .from(postImage)
        //     .innerJoin(postImage.post, post)
        //     .groupBy(post.id)
        //     .fetch();

        return jpaQueryFactory.select(
                Projections.constructor(PostSearchResponse.class,
                    post.id.as("postId"),
                    QPostImages.postImages1.postImages.any().imageUrl.as("imageUrl"),
                    post.title.as("title"),
                    post.createdDateTime.as("createDateTime"),
                    post.location.latitude.as("latitude"),
                    post.location.longitude.as("longitude")
                )
            )
            .distinct()
            .from(postImage)
            .innerJoin(postImage.post, post)
            .innerJoin(post.postTags.postTags, postTag)
            .innerJoin(postTag.tag, tag)
            .on(tag.couple.id.eq(coupleId))
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
