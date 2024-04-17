package com.kidchang.lingopress._base.exception;


import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final Code errorCode;

    public GlobalException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(Code errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}