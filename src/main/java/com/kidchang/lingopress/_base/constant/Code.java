package com.kidchang.lingopress._base.constant;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {

    // Success
    OK(HttpStatus.OK, "OK"),

    // Common
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method 요청입니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 API를 찾을 수 없습니다."),
    QUERY_PARAMETER_REQUIRED(HttpStatus.BAD_REQUEST, "쿼리 파라미터가 필요한 API입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "검증 에러가 발생하였습니다."),

    // Token
    NOT_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "시그니처 검증에 실패한 JWT 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "손상된 JWT 토큰입니다."),
    NOT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 아닙니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    JWT_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "JWT와 관련한 알 수 없는 에러가 발생하였습니다."),
    NOT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 아닙니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    AUTH_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),


    TRANSLATION_ERROR(HttpStatus.BAD_REQUEST, "번역 에러가 발생하였습니다."),

    // Press
    PRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 뉴스를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.getMessage(this.getMessage(this.getMessage() + " - " + throwable.getMessage()));
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
            .filter(Predicate.not(String::isBlank))
            .orElse(this.getMessage());
    }
}