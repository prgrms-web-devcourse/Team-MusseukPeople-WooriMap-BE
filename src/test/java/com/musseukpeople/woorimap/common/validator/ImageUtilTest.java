package com.musseukpeople.woorimap.common.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageUtilTest {

    @DisplayName("이미지 URL 판단 성공")
    @Test
    void isImageUrl_success() {
        // given
        String url = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png";

        // when
        boolean result = ImageUtil.isImageUrl(url);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("URL이 아님으로 인한 실패")
    @Test
    void isImageUrl_notUrl_fail() {
        // given
        String url = "invalidUrl";

        // when
        boolean result = ImageUtil.isImageUrl(url);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("URL이 이미지가 아님으로 인한 실패")
    @Test
    void isImageUrl_invalidImageUrl_fail() {
        // given
        String url = "https://programmers.co.kr/";

        // when
        boolean result = ImageUtil.isImageUrl(url);

        // then
        assertThat(result).isFalse();
    }
}
