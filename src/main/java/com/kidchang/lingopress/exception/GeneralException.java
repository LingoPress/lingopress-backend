package com.kidchang.lingopress.exception;


import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final Code errorCode;

    public GeneralException() {
        super(Code.INTERNAL_ERROR.getMessage());
        this.errorCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(String message) {
        super(Code.INTERNAL_ERROR.getMessage(message));
        this.errorCode = Code.INTERNAL_ERROR;
    }
}