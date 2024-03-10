package com.kidchang.lingopress.press.dto.request;

public record TranslateContentLineRequest(
    Long pressId,
    Integer contentLineNumber,
    String translateText,
    Boolean isCorrect
) {

}
