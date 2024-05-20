package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TextSimilarityAnalysisResponse(
        float similarity,
        int similarityApiUsage,
        String translatedLineText
) {
}
