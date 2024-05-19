package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GlobalException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.apiUsage.ApiUsageEnum;
import com.kidchang.lingopress.apiUsage.ApiUsageTrackerService;
import com.kidchang.lingopress.client.LingoGptClient;
import com.kidchang.lingopress.translate.dto.request.LingoGptRequest;
import com.kidchang.lingopress.translate.dto.response.LingoGptResponse;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateService {

    private final LingoGptClient lingoGptClient;
    private final ApiUsageTrackerService apiUsageTrackerService;

    public TranslateTextResponse translate(LingoGptRequest text) {

        try {
            LingoGptResponse lingoGptResponse = lingoGptClient.translate(text);
            log.info("@@ using token : {}", lingoGptResponse.getToken());
            return TranslateTextResponse.builder()
                    .translatedText(lingoGptResponse.getText())
                    .build();

        } catch (Exception e) {
            throw new GlobalException(Code.TRANSLATION_ERROR, e);
        }
    }

    public TranslateTextResponse translateWithUsageTracker(LingoGptRequest text) {

        Long userId = SecurityUtil.getUserId();

        // 글자수가 너무 길면 예외 발생시키기
        if (text.getOriginal_text().length() > 1200) {
            throw new GlobalException(Code.TRANSLATION_TEXT_TOO_LONG);
        }

        // 번역하기
        TranslateTextResponse translateTextResponse = translate(text);

        // 번역 횟수 업데이트하기
        apiUsageTrackerService.createOrUpdateApiUsageTracker(userId, ApiUsageEnum.TRANSLATION);

        return translateTextResponse;
    }


}