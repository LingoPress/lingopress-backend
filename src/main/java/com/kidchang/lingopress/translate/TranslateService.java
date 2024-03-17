package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.client.DeepLClient;
import com.kidchang.lingopress.translate.dto.request.DeepLRequest;
import com.kidchang.lingopress.translate.dto.request.TranslateTextRequest;
import com.kidchang.lingopress.translate.dto.response.DeepLResponse;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateService {

    private final DeepLClient deepLClient;

    public TranslateTextResponse translate(TranslateTextRequest text) {

        try {
            DeepLRequest deepLRequest = DeepLRequest.builder()
                .text(new String[]{text.getOriginalText()}).target_lang("KO").build();
            DeepLResponse deepLResponse = deepLClient.translate(deepLRequest);
            log.info("deepLResponse: " + deepLResponse);

            return TranslateTextResponse.builder()
                .translatedText(deepLResponse.getTranslations()[0].getText())
                .build();

        } catch (Exception e) {
            throw new GeneralException(Code.TRANSLATION_ERROR, e);
        }
    }

}