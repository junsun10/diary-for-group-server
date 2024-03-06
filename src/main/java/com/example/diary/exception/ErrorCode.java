package com.example.diary.exception;

import jakarta.persistence.NoResultException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT_ERROR"),
    METHOD_ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "METHOD_ARGUMENT_NOT_VALID_ERROR"),
    EMPTY_RESULT_DATA_ACCESS_ERROR(HttpStatus.NOT_FOUND, "EMPTY_RESULT_DATA_ACCESS_ERROR"),
    ILLEGAL_STATE_ERROR(HttpStatus.BAD_REQUEST, "ILLEGAL_STATE_ERROR"),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR"),
    ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "ACCESS_DENIED_ERROR"),
    ;

    private final HttpStatus httpStatus;
    private final String name;
}
