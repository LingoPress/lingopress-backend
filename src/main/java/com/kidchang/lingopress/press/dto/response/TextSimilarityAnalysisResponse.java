package com.kidchang.lingopress.press.dto.response;

import lombok.Builder;

@Builder
public record TextSimilarityAnalysisResponse(
        float similarity,
        int similarityApiUsage
) {
}
