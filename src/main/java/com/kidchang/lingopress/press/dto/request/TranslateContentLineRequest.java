package com.kidchang.lingopress.press.dto.request;

public record TranslateContentLineRequest(
    Long pressId,
    Long contentLineNumber,
    String translateText,
    Boolean isCorrect
) {

}
