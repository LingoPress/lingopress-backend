package com.kidchang.lingopress.press.dto.request;

public record TranslateContentLineMemoRequest(
        Long pressId,
        Integer contentLineNumber,
        String memo) {
}
