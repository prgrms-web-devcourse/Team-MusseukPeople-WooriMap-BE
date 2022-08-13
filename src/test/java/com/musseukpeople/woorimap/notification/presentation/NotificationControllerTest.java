package com.musseukpeople.woorimap.notification.presentation;

import static com.musseukpeople.woorimap.notification.domain.Notification.NotificationType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimap.notification.domain.Notification;
import com.musseukpeople.woorimap.notification.domain.NotificationRepository;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class NotificationControllerTest extends AcceptanceTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("알림 구독 성공")
    @Test
    void subscribe_success() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test")).substring(6);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/subscribe")
                .param("token", accessToken)
                .contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
            .andDo(print())
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("알림 읽음 처리 성공")
    @Test
    void readNotification_success() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        Long notificationId = createNotification();

        // when
        MockHttpServletResponse response = readNotification(accessToken, notificationId);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재 하지 않는 알림으로 인한 읽음 처리 실패")
    @Test
    void readNotification_notFound_success() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        Long notificationId = 1L;

        // when
        MockHttpServletResponse response = readNotification(accessToken, notificationId);

        // then
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("존재하지 않는 알림입니다.")
        );
    }

    @DisplayName("읽지 않은 알림 조회 성공")
    @Test
    void showNotification() throws Exception {
        // given
        String accessToken = 회원가입_토큰(new SignupRequest("test@gmail.com", "!Test1234", "test"));
        readNotification(accessToken, createNotification());
        createNotification();

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/notifications")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn().getResponse();

        // then
        List<NotificationResponse> notificationResponses = getResponseList(response, NotificationResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(notificationResponses).hasSize(1)
        );
    }

    private Long createNotification() {
        return notificationRepository.save(new Notification(1L, 1L, 1L, POST_CREATED, "test")).getId();
    }

    private MockHttpServletResponse readNotification(String accessToken, Long notificationId) throws Exception {
        return mockMvc.perform(patch("/api/notifications/{id}", notificationId)
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();
    }
}
