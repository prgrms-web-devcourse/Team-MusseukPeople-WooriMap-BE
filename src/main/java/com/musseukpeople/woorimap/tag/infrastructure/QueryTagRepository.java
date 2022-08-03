package com.musseukpeople.woorimap.tag.infrastructure;

import java.util.List;

import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.entity.Tag;

public interface QueryTagRepository {
    List<Tag> findExistTagByCoupleId(
        Long coupleId,
        List<TagRequest> tagList);
}
