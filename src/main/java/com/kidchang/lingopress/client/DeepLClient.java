package com.kidchang.lingopress.client;

import com.kidchang.lingopress.translate.dto.request.DeepLRequest;
import com.kidchang.lingopress.translate.dto.response.DeepLResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "deepl", url = "https://api-free.deepl.com")
public interface DeepLClient {

    @PostMapping(value = "/v2/translate")
    DeepLResponse translate(DeepLRequest request);

}
