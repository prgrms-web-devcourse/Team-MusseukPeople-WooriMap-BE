package com.musseukpeople.woorimap.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	//Common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", " 잘못된 입력 값입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", " 메소드를 사용할 수 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", " 서버 에러입니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", " 잘못된 타입입니다."),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", " 접근 권한이 없습니다.");

	//User

	//Couple

	//Post

	private final String code;
	private final String message;
	private final HttpStatus status;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
