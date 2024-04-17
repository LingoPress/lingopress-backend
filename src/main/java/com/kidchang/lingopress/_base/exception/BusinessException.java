package com.kidchang.lingopress._base.exception;


import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Code errorCode;

    public BusinessException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(Code errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}