package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressContentLineResponse(
    Long id,
    Long pressContentLineNumber,
    String userTranslatedLineText,
    String machineTranslatedLineText,

    String originalLineText,
    Boolean isCorrect
) {

    public static PressContentLineResponse from(Long id, Long pressContentLineNumber,
        String userTranslatedLine, String machineTranslatedLine, String originalLine,
        Boolean isCorrect) {
        return new PressContentLineResponse(id, pressContentLineNumber, userTranslatedLine,
            machineTranslatedLine, originalLine, isCorrect);
    }

}
