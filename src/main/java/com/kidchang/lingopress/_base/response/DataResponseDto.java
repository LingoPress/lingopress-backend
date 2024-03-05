package com.kidchang.lingopress._base.response;

import com.kidchang.lingopress._base.constant.Code;
import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    private final T data;

    private DataResponseDto(T data) {
        super(Code.OK.toString(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponseDto(Code code, T data) {
        super(Code.OK.toString(), Code.OK.getMessage());
        this.data = data;
    }

    public static <T> DataResponseDto<T> of(Code code, T data) {
        return new DataResponseDto<>(code, data);
    }
    public static <T> DataResponseDto<T> of(T data) {
        return new DataResponseDto<>(data);
    }
}