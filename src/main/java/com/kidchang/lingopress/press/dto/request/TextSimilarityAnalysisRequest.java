package com.kidchang.lingopress.press.dto.request;

import lombok.Builder;

@Builder
public record TextSimilarityAnalysisRequest(
        String original_text,
        String compared_text,
        Long press_id,
        int line_number,
        // 원문
        String originalText

) {
}
