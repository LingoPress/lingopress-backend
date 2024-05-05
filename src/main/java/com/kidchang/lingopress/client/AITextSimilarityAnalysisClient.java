package com.kidchang.lingopress.client;

import com.kidchang.lingopress.press.dto.request.TextSimilarityAnalysisRequest;
import com.kidchang.lingopress.press.dto.response.TextSimilarityAnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "text-similarity", url = "${lingo-ai.url}")
public interface AITextSimilarityAnalysisClient {

    @PostMapping(value = "/text_similarity")
    TextSimilarityAnalysisResponse checkPressContentLineSimilarity(TextSimilarityAnalysisRequest request);

}
