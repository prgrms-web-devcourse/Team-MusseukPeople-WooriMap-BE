package com.musseukpeople.woorimap.notification.presentation;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.notification.application.NotificationService;
import com.musseukpeople.woorimap.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimap.notification.presentation.resolver.LoginForNoti;
import com.musseukpeople.woorimap.notification.presentation.resolver.LoginRequiredForNoti;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "알림", description = "알림 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 구독", description = "알림 구독 API입니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @LoginRequiredForNoti
    public SseEmitter subscribe(@LoginForNoti LoginMember loginMember,
                                @RequestHeader(value = "last-event-id", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(loginMember.getId(), lastEventId);
    }

    @Operation(summary = "알림 조회", description = "읽지 않은 알림 조회 API입니다.")
    @GetMapping("/notifications")
    @LoginRequired
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> showNotifications(@Login LoginMember loginMember) {
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(loginMember.getId());
        return ResponseEntity.ok(new ApiResponse<>(notifications));
    }

    @Operation(summary = "알림 읽음", description = "알림 읽음 API입니다.")
    @PatchMapping("/notifications/{id}")
    @LoginRequired
    public ResponseEntity<Void> readNotification(@PathVariable("id") Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.noContent().build();
    }
}
