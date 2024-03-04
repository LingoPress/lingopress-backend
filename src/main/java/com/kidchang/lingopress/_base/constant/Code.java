package com.kidchang.lingopress._base.constant;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {
    OK(200, HttpStatus.OK, "OK"),
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(2001, HttpStatus.BAD_REQUEST, "검증 에러가 발생하였습니다."),
    TYPE_MISMATCH(2002, HttpStatus.BAD_REQUEST, "타입이 일치하지 않습니다."),

    // User

    NOT_FOUND_USER(3001, HttpStatus.NOT_FOUND, "User not found"),
    EMPTY_EMAIL(3002, HttpStatus.BAD_REQUEST, "Email is empty"),
    EMPTY_NAME(3003, HttpStatus.BAD_REQUEST, "Name is empty"),
    ALREADY_EXISTS_USER(3004, HttpStatus.BAD_REQUEST, "Already exists user"),
    EMPTY_PASSWORD(3005, HttpStatus.BAD_REQUEST, "Password is empty"),
    INTERNAL_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
//        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        return this.getMessage(e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
            .filter(Predicate.not(String::isBlank))
            .orElse(this.getMessage());
    }


}