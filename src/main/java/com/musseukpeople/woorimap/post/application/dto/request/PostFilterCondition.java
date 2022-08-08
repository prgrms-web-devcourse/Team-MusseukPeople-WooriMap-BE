package com.musseukpeople.woorimap.post.application.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostFilterCondition {

    private List<Long> tagIds;
    private String title;
    private Long lastPostId;
    private int paginationSize = 20;

    public PostFilterCondition(List<Long> tagIds, String title, Long lastPostId) {
        this.tagIds = tagIds;
        this.title = title;
        this.lastPostId = lastPostId;
    }
}
