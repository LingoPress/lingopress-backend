package com.kidchang.lingopress.press.dto.request;

public record TextSimilarityAnalysisRequest(
        String original_text,
        String compared_text
) {
}
