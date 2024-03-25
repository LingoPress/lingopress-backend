package com.kidchang.lingopress.client;

import com.kidchang.lingopress.translate.dto.request.LingoGptRequest;
import com.kidchang.lingopress.translate.dto.response.LingoGptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "lingo-gpt", url = "localhost:8000")
public interface LingoGptClient {

    @PostMapping(value = "/translate/word")
    LingoGptResponse translate(LingoGptRequest request);
}
