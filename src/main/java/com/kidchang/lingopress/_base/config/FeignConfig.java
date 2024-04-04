package com.kidchang.lingopress._base.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.kidchang.lingopress.client")
public class FeignConfig {

    //    @Value("${deepl.auth-key}")
    @Value("${chatgpt.auth-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
//            template.header("Authorization", "DeepL-Auth-Key " + apiKey);
            template.header("api-key", apiKey);
            template.header("Accept", "application/json");
        };
    }

}
