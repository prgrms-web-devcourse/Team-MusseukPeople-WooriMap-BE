package com.musseukpeople.woorimap.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", " 잘못된 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", " 메소드를 사용할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", " 서버 에러입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", " 잘못된 타입입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", " 접근 권한이 없습니다."),

    // User
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "U001", "이메일 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "U002", "중복된 이메일입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "U003", "존재하지 않는 사용자입니다."),

    // Auth
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "로그인이 필요합니다."),
    BLACKLIST_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "허용되지 않는 토큰입니다."),

    // Couple
    NOT_FOUND_COUPLE(HttpStatus.NOT_FOUND, "CP001", "존재하지 않는 커플입니다."),
    NOT_CREATE_COUPLE(HttpStatus.BAD_REQUEST, "CP002", "커플 맺기 실패했습니다"),
    ALREADY_COUPLE(HttpStatus.BAD_REQUEST, "CP003", "이미 커플입니다."),
    INVALID_COUPLE_MEMBERS_SIZE(HttpStatus.BAD_REQUEST, "CP004", "커플 멤버의 수가 유효하지 않습니다."),
    NOT_MAPPING_COUPLE_MEMBER(HttpStatus.BAD_REQUEST, "CP005", "커플 멤버 정보 매핑 실패"),

    // InviteCode
    NOT_FOUND_INVITE_CODE(HttpStatus.NOT_FOUND, "N001", "존재하지 않는 코드입니다."),

    // Post
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 게시글입니다."),
    DUPLICATE_TAG(HttpStatus.BAD_REQUEST, "P002", "태그가 중복됩니다."),
    NOT_BELONG_TO_COUPLE(HttpStatus.FORBIDDEN, "P003", "해당하는 사용자의 게시물이 아닙니다."),

    // Image
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "I001", "지원하지 않는 이미지 확장자입니다."),
    EXCEED_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "I002", "이미지 용량이 너무 큽니다."),

    // Notification
    NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, "N001", "존재하지 않는 알림입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
