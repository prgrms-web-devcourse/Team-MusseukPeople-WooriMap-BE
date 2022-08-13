package com.musseukpeople.woorimapimage.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimapimage.image.exception.FileException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(HttpClientErrorException e)
        throws JsonProcessingException {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorResponse errorResponse = new ObjectMapper().readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        return ResponseEntity
            .status(e.getRawStatusCode())
            .body(errorResponse);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileExpcetion(FileException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(MaxUploadSizeExceededException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.EXCEED_IMAGE_SIZE;
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(errorCode));
    }
}
