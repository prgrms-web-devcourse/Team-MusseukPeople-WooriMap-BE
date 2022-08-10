package com.musseukpeople.woorimap.tag.infrastructure;

import java.util.List;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.domain.Tag;

public interface QueryTagRepository {
    List<Tag> findExistTagByCouple(Couple couple, List<String> tagNames);
}
