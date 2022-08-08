package com.musseukpeople.woorimap.post.infrastructure;

import java.util.List;

import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.domain.Post;

public interface PostQueryRepository {

    List<Post> findPostsByFilterCondition(PostFilterCondition postFilterCondition, Long coupleId);
}
