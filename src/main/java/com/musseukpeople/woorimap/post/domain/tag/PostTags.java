package com.musseukpeople.woorimap.post.domain.tag;

import static com.google.common.base.Preconditions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.exception.DuplicateTagException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTags {

    private static final int POST_TAGS_MIN_SIZE = 1;
    private static final String POST_TAGS_MIN_SIZE_MESSAGE = "태그는 1개 이상 등록해야합니다";

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    public PostTags(List<PostTag> postTags) {
        validateDuplicate(postTags);
        checkArgument(postTags.size() >= POST_TAGS_MIN_SIZE, POST_TAGS_MIN_SIZE_MESSAGE);
        this.postTags = new HashSet<>(postTags);
    }

    private void validateDuplicate(List<PostTag> postTags) {
        long nonDuplicateTagSize = postTags.stream()
            .map(PostTag::getTag)
            .map(Tag::getName)
            .distinct()
            .count();

        if (nonDuplicateTagSize != postTags.size()) {
            throw new DuplicateTagException(ErrorCode.DUPLICATE_TAG);
        }
    }

    public void changePostTags(List<PostTag> postTagsToAdd) {
        checkArgument(postTags.size() >= POST_TAGS_MIN_SIZE, POST_TAGS_MIN_SIZE_MESSAGE);
        postTags.clear();
        postTags.addAll(postTagsToAdd);
    }
}
