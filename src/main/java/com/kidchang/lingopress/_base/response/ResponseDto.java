package com.kidchang.lingopress._base.response;

import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class ResponseDto {

    private final String code;
    private final String message;

    public static ResponseDto of(Code code) {
        return new ResponseDto(code.toString(), code.getMessage());
    }
}