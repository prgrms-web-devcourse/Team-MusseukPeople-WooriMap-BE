package com.musseukpeople.woorimap.notification.presentation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.notification.presentation.resolver.LoginForNoti;
import com.musseukpeople.woorimap.notification.presentation.resolver.LoginRequiredForNoti;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @LoginRequiredForNoti
    public SseEmitter subscribe(@LoginForNoti LoginMember loginMember,
                                @RequestHeader(value = "last-event-id", required = false, defaultValue = "") String lastEventId) {
        return new SseEmitter();
    }
}
