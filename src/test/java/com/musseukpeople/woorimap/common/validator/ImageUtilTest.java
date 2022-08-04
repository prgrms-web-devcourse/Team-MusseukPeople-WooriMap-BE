package com.musseukpeople.woorimap.common.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageUtilTest {

    @DisplayName("이미지 URL 판단 성공")
    @Test
    void isImageUrl_success() {
        // given
        String url = "https://main.dwkg3ddyxqleg.amplifyapp.com/8c7f4d11b6e0b8ac08f0.png";

        // when
        boolean result = ImageUtil.isImageUrl(url);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("올바르지 않는 URL로 인한 실패")
    @ParameterizedTest
    @ValueSource(strings = {"invalidUrl", "https://programmers.co.kr/", "https://test.com"})
    void isImageUrl_notUrl_fail(String url) {
        // given
        // when
        boolean result = ImageUtil.isImageUrl(url);

        // then
        assertThat(result).isFalse();
    }
}
