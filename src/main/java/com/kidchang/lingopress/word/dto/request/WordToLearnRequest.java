package com.kidchang.lingopress.word.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record WordToLearnRequest(
        String word,
        String originalText,
        String translatedText,
        Integer lineNumber,
        Long pressId,
        LanguageEnum language
) {

}
