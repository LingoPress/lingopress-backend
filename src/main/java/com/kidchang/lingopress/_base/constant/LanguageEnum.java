package com.kidchang.lingopress._base.constant;

import lombok.Getter;

@Getter
public enum LanguageEnum {
    ENGLISH("en"),
    JAPANESE("ja"),
    KOREAN("ko");

    private final String value;

    LanguageEnum(String value) {
        this.value = value;
    }

}