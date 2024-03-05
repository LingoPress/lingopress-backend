package com.kidchang.lingopress._base.exception;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.response.ErrorResponseDto;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Code 예외 처리
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<Object> handleBusinessException(GeneralException e) {
        log.error(e.toString(), e);
        return handleExceptionInternal(e.getErrorCode());
    }


    // 지원하지 않는 HTTP method를 호출할 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return handleExceptionInternal(Code.METHOD_NOT_ALLOWED);
    }

    // 존재하지 않는 URI에 접근할 경우
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<Object> handleNoResourceFoundException() {
        return handleExceptionInternal(Code.API_NOT_FOUND);
    }

    // 쿼리 파라미터 없이 요청할 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameterException() {
        return handleExceptionInternal(Code.QUERY_PARAMETER_REQUIRED);
    }

    // @Valid 유효성 검증에 실패할 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error(e.toString(), e);
        return handleExceptionInternal(Code.INVALID_INPUT_VALUE,
            e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    // 그 밖에 발생하는 모든 예외 처리
    @ExceptionHandler(value = {Exception.class, RuntimeException.class, SQLException.class,
        DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleException(Exception e) {
        log.error(e.toString(), e);

        return handleExceptionInternal(Code.INTERNAL_ERROR, e);
    }

    private ResponseEntity<Object> handleExceptionInternal(Code errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(ErrorResponseDto.from(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(Code errorCode, Exception e) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(ErrorResponseDto.of(errorCode, e));
    }

    private ResponseEntity<Object> handleExceptionInternal(Code errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(ErrorResponseDto.of(errorCode, message));
    }
}
