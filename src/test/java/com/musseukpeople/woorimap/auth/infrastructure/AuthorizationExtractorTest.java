package com.musseukpeople.woorimap.auth.infrastructure;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

class AuthorizationExtractorTest {

    @DisplayName("헤더 토큰 추출 성공")
    @Test
    void extreact_success() {
        // given
        String headerToken = "Bearer token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, headerToken);

        // when
        Optional<String> token = AuthorizationExtractor.extract(request);

        // then
        assertThat(token).isNotEmpty()
            .get()
            .isEqualTo("token");
    }

    @DisplayName("헤더에 값이 없음으로 인한 추출 실패")
    @Test
    void extreact_null_fail() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        Optional<String> token = AuthorizationExtractor.extract(request);

        // then
        assertThat(token).isEmpty();
    }
}
