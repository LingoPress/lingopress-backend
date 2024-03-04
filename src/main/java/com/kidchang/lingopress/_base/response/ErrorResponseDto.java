package com.kidchang.lingopress._base.response;

import com.kidchang.lingopress._base.constant.Code;

public class ErrorResponseDto extends ResponseDto {

    private ErrorResponseDto(Code errorCode) {
        super(errorCode.toString(), errorCode.getMessage());
    }

    private ErrorResponseDto(Code errorCode, Exception e) {
        super(errorCode.toString(), errorCode.getMessage(e));
    }

    private ErrorResponseDto(Code errorCode, String message) {
        super(errorCode.toString(), errorCode.getMessage() + " - " + message);
    }

    public static ErrorResponseDto from(Code errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(Code errorCode, Exception e) {
        return new ErrorResponseDto(errorCode, e);
    }

    public static ErrorResponseDto of(Code errorCode, String message) {
        return new ErrorResponseDto(errorCode, message);
    }
}