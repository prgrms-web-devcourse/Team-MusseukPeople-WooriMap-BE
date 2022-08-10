package com.musseukpeople.woorimap.notification.fake;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/fake")
@Slf4j
public class FakeNotificationController {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("id") String memberId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        String id = memberId + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        log.error("id 값 : {}, 생성 sse : {}", id, sseEmitter);
        sseEmitters.put(id, sseEmitter);
        log.error("저장 sse : {}, {}", id, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(id));
        sseEmitter.onTimeout(() -> sseEmitters.remove(id));

        sendToClient(sseEmitter, id, "EventStream Created. [userId=" + id + "]");

        return sseEmitter;
    }

    @PostMapping("/publish/{id}")
    public void sendMessage(@PathVariable("id") String receiverId, @RequestBody MessageDto messageDto) {
        Map<String, SseEmitter> sseEmitterMap = sseEmitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(receiverId))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        log.error("찾은 sse : {}", sseEmitterMap.size());

        sseEmitterMap.forEach(
            (key, sseEmitter) -> {
                log.error("보내는 id : {} , sse : {}", key, sseEmitter);
                sendToClient(sseEmitter, key, messageDto.getMessage());
            }
        );
    }

    private void sendToClient(SseEmitter sseEmitter, String id, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(id)
                .name("test")
                .data(data));
        } catch (IOException e) {
            sseEmitters.remove(id);
            throw new IllegalStateException("연결 오류");
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class MessageDto {

        String message;

        public MessageDto(String message) {
            this.message = message;
        }
    }
}
