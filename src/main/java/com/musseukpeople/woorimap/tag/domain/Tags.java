package com.musseukpeople.woorimap.tag.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;

public class Tags {

    private final List<Tag> tags;

    public Tags(List<Tag> tags) {
        validateDuplicate(toNames(tags));
        this.tags = tags;
    }

    private void validateDuplicate(List<String> tagNames) {
        Set<String> nonDuplicateTagNames = new HashSet<>(tagNames);
        if (tagNames.size() != nonDuplicateTagNames.size()) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG);
        }
    }

    public List<String> toNames() {
        return tags.stream()
            .map(Tag::getName)
            .collect(toList());
    }

    public Tags addAll(Tags newTags) {
        List<Tag> addTags = new ArrayList<>(this.tags);
        addTags.addAll(newTags.tags);
        return new Tags(addTags);
    }

    public Tags removeAllByName(Tags compareTags) {
        return tags.stream()
            .filter(tag -> compareTags.tags.stream().noneMatch(tag::isSameName))
            .collect(collectingAndThen(toList(), Tags::new));
    }

    public List<Tag> getList() {
        return this.tags;
    }

    private List<String> toNames(List<Tag> tags) {
        return tags.stream()
            .map(Tag::getName)
            .collect(toList());
    }
}
