package com.musseukpeople.woorimap.tag.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TagNameTest {

    @DisplayName("태그 이름 생성 성공")
    @ParameterizedTest(name = "이름 : {0}")
    @ValueSource(strings = {"서울", "seoul", "서울 낙성대역 맛집", "서울 낙성대역 맛집     "})
    void create_success(String name) {
        // given
        // when
        TagName tagName = new TagName(name);

        // then
        assertThat(tagName).isNotNull();
    }

    @DisplayName("이름이 비어있음으로 인한 생성 실패")
    @ParameterizedTest(name = "이름 : {0}")
    @NullAndEmptySource
    void create_nullOrEmpty_fail(String name) {
        assertThatThrownBy(() -> new TagName(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("태그 이름은 비어있을 수 없습니다.");
    }

    @DisplayName("이름이 최대 길이를 넘음으로 인한 생성 실패")
    @ParameterizedTest(name = "이름 : {0}")
    @ValueSource(strings = {"서울 낙성대역 맛집임", "서울 낙성대역 맛집임 진짜"})
    void create_overMaxLength_fail(String name) {
        assertThatThrownBy(() -> new TagName(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("초과할 수 없습니다.");
    }
}
