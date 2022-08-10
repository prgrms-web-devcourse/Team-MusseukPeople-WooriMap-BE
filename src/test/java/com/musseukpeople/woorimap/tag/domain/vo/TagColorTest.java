package com.musseukpeople.woorimap.tag.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TagColorTest {

    @DisplayName("태그 색깔 생성 성공")
    @ParameterizedTest(name = "색깔 : {0}")
    @ValueSource(strings = {"#FFFFFF", "#123456", "   #800000", "R(255)G(255)B(255)", "R(255) G(255) B(255)", "240, 248, 255"})
    void create_success(String color) {
        // given
        // when
        TagColor tagColor = new TagColor(color);

        // then
        assertThat(tagColor).isNotNull();
    }

    @DisplayName("색깔이 비어있음으로 인한 생성 실패")
    @ParameterizedTest(name = "색깔 : {0}")
    @NullAndEmptySource
    void create_nullOrEmpty_fail(String color) {
        assertThatThrownBy(() -> new TagColor(color))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("태그 색깔은 비어있을 수 없습니다.");
    }

    @DisplayName("색깔이 지정 길이가 아님으로 인한 생성 실패")
    @ParameterizedTest(name = "색깔 : {0}")
    @ValueSource(strings = {"#FFFFF", "   #FFFFF", "R ( 255 ) G ( 255 ) B ( 255 )"})
    void create_notEqualLength_fail(String color) {
        assertThatThrownBy(() -> new TagColor(color))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
