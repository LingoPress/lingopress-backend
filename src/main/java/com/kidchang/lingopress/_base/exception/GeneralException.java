package com.kidchang.lingopress._base.exception;


import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final Code errorCode;

    public GeneralException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}