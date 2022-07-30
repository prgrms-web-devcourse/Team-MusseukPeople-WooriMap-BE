package com.musseukpeople.woorimap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "헬스 체크", description = "헬스 체크 API입니다.")
@Slf4j
@RestController
public class HealthCheckController {

    @Operation(summary = "서버 실행 헬스 체크", description = "서버 실행 여부를 확인합니다.")
    @GetMapping("/health")
    public String check() {
        return "Check This Sound";
    }

    @Operation(summary = "로그 헬스 체크", description = "로그 설정 여부를 확인합니다.")
    @GetMapping("/health/log")
    public String checkLog() {
        log.debug("debug 로그 체크");
        log.info("info 로그 체크");
        log.warn("warn 로그 체크");
        log.error("error 로그 체크");
        return "Check Logs";
    }

}
