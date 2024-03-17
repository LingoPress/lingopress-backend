package com.kidchang.lingopress.word.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record WordToLearnResponse(
    Long id,
    String word,
    String translatedWord,
    String originalLineText,
    Integer lineNumber,
    String isLearned
) {

}
