package com.musseukpeople.woorimap.notification.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class NotificationControllerTest extends AcceptanceTest {

    @DisplayName("알림 구독 성공")
    @Test
    void subscribe_success() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        accessToken = 커플_맺기_토큰(accessToken).substring(6);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/subscribe")
                .param("token", accessToken)
                .contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
            .andDo(print())
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
