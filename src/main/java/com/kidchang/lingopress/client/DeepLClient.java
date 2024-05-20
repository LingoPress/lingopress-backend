package com.kidchang.lingopress.client;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@FeignClient(name = "deepl", url = "https://api-free.deepl.com")
//public interface DeepLClient {
//
//    @PostMapping(value = "/v2/translate")
//    DeepLResponse translate(DeepLRequest request);
//
//}

@Component
public class DeepLClient {

    Translator translator;


    public DeepLClient(@Value("${deepl.auth-key}") String apiKey) {
        translator = new Translator(apiKey);
    }

    public TextResult translate(String text, String sourceLang, String targetLang) throws DeepLException, InterruptedException {
        return translator.translateText(text, sourceLang, targetLang);
    }


}