package com.musseukpeople.woorimap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "헬스 체크", description = "헬스 체크 API입니다.")
@RestController
public class HealthCheckController {

	@Operation(summary = "헬스 체크", description = "서버 실행 여부를 확인합니다.")
	@GetMapping("/health")
	public String check() {
		return "Check This Sound";
	}

}
