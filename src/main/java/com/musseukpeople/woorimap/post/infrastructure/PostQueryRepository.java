package com.musseukpeople.woorimap.post.infrastructure;

import java.util.List;

import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.application.dto.response.PostSearchResponse;

public interface PostQueryRepository {

    List<PostSearchResponse> findPostsByFilterCondition(PostFilterCondition postFilterCondition, Long coupleId);
}
