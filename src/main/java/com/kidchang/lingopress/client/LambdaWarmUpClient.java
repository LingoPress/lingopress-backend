package com.kidchang.lingopress.client;

import com.kidchang.lingopress._base.response.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "lambda-warmup", url = "${lingo-ai.url}")
public interface LambdaWarmUpClient {

    @GetMapping(value = "/warming-up")
    ResponseDto warmUp();

}
