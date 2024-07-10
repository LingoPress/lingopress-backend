package com.kidchang.lingopress._base.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    // Success
    OK(HttpStatus.OK, "OK"),

    // Common: Global
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method 요청입니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 API를 찾을 수 없습니다."),
    QUERY_PARAMETER_REQUIRED(HttpStatus.BAD_REQUEST, "쿼리 파라미터가 필요한 API입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTEGRITY_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "데이터 무결성 에러가 발생하였습니다."),
    ERROR_OCCURRED_TEST(HttpStatus.BAD_REQUEST, "에러 발생 테스트입니다."),

    // Token: Business
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

    // User: Business
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    DUPLICATED_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    AUTH_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),
    NOT_ACCESSIBLE_USER(HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다."),
    FAILED_TO_GET_GOOGLE_USER_INFO(HttpStatus.BAD_REQUEST, "구글 사용자 정보를 가져오는데 실패하였습니다."),
    FAILED_TO_GET_GOOGLE_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "구글 액세스 토큰을 가져오는데 실패하였습니다."),
    FAILED_TO_DELETE_USER(HttpStatus.BAD_REQUEST, "유저 삭제에 실패하였습니다."),
    USER_IS_INACTIVE(HttpStatus.FORBIDDEN, "비활성화된 사용자입니다. 관리자에게 문의하세요."),
    FAILED_TO_UPDATE_USER_LANGUAGE(HttpStatus.BAD_REQUEST, "유저 언어 정보 수정에 실패하였습니다."),


    // Press
    PRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 뉴스를 찾을 수 없습니다."),

    NOT_FOUND_LEARNING_RECORD(HttpStatus.NOT_FOUND, "학습 기록을 찾을 수 없습니다."),
    SIMILARITY_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "유사도 검사 횟수 하루 사용량을 초과하였습니다."),
    PRESS_CONTENT_LINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 뉴스 문장을 찾을 수 없습니다."),

    // email
    MESSAGING_EXCEPTION(HttpStatus.BAD_REQUEST, "메일 전송에 실패하였습니다."),

    // video
    VIDEO_TRANSCRIPTION_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "동영상 자막 추출 횟수 하루 사용량을 초과하였습니다."),

    // rabbitmq
    RABBITMQ_MESSAGE_RECEIVE_ERROR(HttpStatus.BAD_REQUEST, "rabbitmq 메시지 수신에 실패하였습니다."),

    // Translate: Global
    TRANSLATION_ERROR(HttpStatus.BAD_REQUEST, "번역 에러가 발생하였습니다."),
    TRANSLATION_TEXT_TOO_LONG(HttpStatus.BAD_REQUEST, "번역할 텍스트가 너무 깁니다. 정상적인 문장이라면 제보해주세요."),
    // Translate: Business
    TRANSLATION_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "오늘 번역 횟수 제한(50번)을 초과하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
//        return this.getMessage() + " - " + throwable.getMessage();
        return this.getMessage();
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}