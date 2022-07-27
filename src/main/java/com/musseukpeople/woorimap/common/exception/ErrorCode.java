package com.musseukpeople.woorimap.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	//Common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", " Invalid Input Value"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", " Invalid Input Value"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Server Error"),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", " Invalid Type Value"),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "Access is Denied");

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
