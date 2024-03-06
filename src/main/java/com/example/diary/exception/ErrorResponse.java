package com.example.diary.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
    private String errorName;
    private String message;
    private String path;

    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = errorCode.getHttpStatus();
        this.message = message;
        this.errorName = errorCode.getName();
        this.path = path;
    }
}