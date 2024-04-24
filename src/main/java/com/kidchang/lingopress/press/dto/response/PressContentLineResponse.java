package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.press.entity.PressContentLine;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressContentLineResponse(
        Long id,
        Integer pressContentLineNumber,
        String userTranslatedLineText,
        String machineTranslatedLineText,
        String originalLineText,
        Boolean isCorrect,
        String memo
) {

    public static PressContentLineResponse from(PressContentLine pressContentLine) {
        return PressContentLineResponse.builder()
                .id(pressContentLine.getId())
                .pressContentLineNumber(pressContentLine.getLineNumber())
                .machineTranslatedLineText(pressContentLine.getTranslatedLineText())
                .originalLineText(pressContentLine.getLineText())
                .build();
    }
}
