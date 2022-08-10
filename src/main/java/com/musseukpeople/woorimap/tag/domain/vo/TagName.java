package com.musseukpeople.woorimap.tag.domain.vo;

import static com.google.common.base.Preconditions.*;
import static java.text.MessageFormat.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagName {

    private static final int MAX_LENGTH = 10;

    @Column(name = "name", nullable = false, length = MAX_LENGTH)
    private String value;

    public TagName(String name) {
        checkArgument(!StringUtils.isBlank(name), "태그 이름은 비어있을 수 없습니다.");
        checkArgument(name.trim().length() <= MAX_LENGTH, format("태그 이름은 {0}글자를 초과할 수 없습니다.", MAX_LENGTH));
        this.value = name.trim();
    }
}
