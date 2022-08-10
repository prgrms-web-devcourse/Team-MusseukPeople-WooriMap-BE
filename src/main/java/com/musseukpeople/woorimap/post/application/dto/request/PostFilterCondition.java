package com.musseukpeople.woorimap.post.application.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostFilterCondition {

    @Schema(description = "태그 아이디")
    private List<Long> tagIds;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "마지막 게시글 아이디")
    private Long lastPostId;

    @Schema(description = "사이즈")
    private int paginationSize = 20;

    public PostFilterCondition(List<Long> tagIds, String title, Long lastPostId) {
        this.tagIds = tagIds;
        this.title = title;
        this.lastPostId = lastPostId;
    }
}
