package com.kidchang.lingopress.press.dto.request;

import lombok.Builder;

@Builder
public record TextSimilarityAnalysisRequest(
        // 기계번역
        String machineTranslatedText,
        // 사용자가 입력한 번역
        String userTranslatedText,
        Long press_id,
        int line_number,
        // 원문
        String originalText

) {
}
