package com.example.diary.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 검사시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            HttpServletRequest request, BindingResult bindingResult) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.METHOD_ARGUMENT_NOT_VALID_ERROR,
                bindingResult.getFieldError().getDefaultMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.METHOD_ARGUMENT_NOT_VALID_ERROR.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            HttpServletRequest request, IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.ILLEGAL_ARGUMENT_ERROR,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(
            HttpServletRequest request, EmptyResultDataAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.EMPTY_RESULT_DATA_ACCESS_ERROR,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.EMPTY_RESULT_DATA_ACCESS_ERROR.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            HttpServletRequest request, IllegalStateException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.ILLEGAL_STATE_ERROR,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.ILLEGAL_STATE_ERROR.getHttpStatus()).body(errorResponse);
    }

    //미인증
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            HttpServletRequest request, AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.AUTHENTICATION_ERROR,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED_ERROR.getHttpStatus()).body(errorResponse);
    }

    //미인가
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            HttpServletRequest request, AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.ACCESS_DENIED_ERROR,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED_ERROR.getHttpStatus()).body(errorResponse);
    }
}