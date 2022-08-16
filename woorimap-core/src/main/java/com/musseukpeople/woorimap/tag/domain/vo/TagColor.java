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
public class TagColor {

    private static final int COLOR_MIN_LENGTH = 7;
    private static final int COLOR_MAX_LENGTH = 25;

    @Column(name = "color", nullable = false, length = COLOR_MAX_LENGTH)
    private String value;

    public TagColor(String color) {
        checkArgument(!StringUtils.isBlank(color), "태그 색깔은 비어있을 수 없습니다.");
        checkArgument(color.trim().length() >= COLOR_MIN_LENGTH, format("태그 색깔 길이는 {0} 이상이어야 합니다.", COLOR_MIN_LENGTH));
        checkArgument(color.trim().length() <= COLOR_MAX_LENGTH, format("태그 색깔 길이는 {0} 이하이어야 합니다.", COLOR_MAX_LENGTH));
        value = color.trim();
    }
}
