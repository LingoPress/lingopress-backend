package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.client.DeepLClient;
import com.kidchang.lingopress.translate.dto.request.DeepLRequest;
import com.kidchang.lingopress.translate.dto.request.TranslateTextRequest;
import com.kidchang.lingopress.translate.dto.response.DeepLResponse;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateService {

    private final DeepLClient deepLClient;
    private final TranslateApiUsageTrackerRepository translateApiUsageTrackerRepository;

    public TranslateTextResponse translate(TranslateTextRequest text) {

        try {
            DeepLRequest deepLRequest = DeepLRequest.builder()
                .text(new String[]{text.getOriginalText()}).target_lang("KO").build();
            DeepLResponse deepLResponse = deepLClient.translate(deepLRequest);

            return TranslateTextResponse.builder()
                .translatedText(deepLResponse.getTranslations()[0].getText())
                .build();

        } catch (Exception e) {
            throw new GeneralException(Code.TRANSLATION_ERROR, e);
        }
    }

    @Transactional
    public TranslateTextResponse translateWithUsageTracker(TranslateTextRequest text) {

        Long userId = SecurityUtil.getUserId();
        // 1. 번역 횟수 기록하기
        TranslateApiUsageTracker tracker = translateApiUsageTrackerRepository.findByUserId(userId);

        if (tracker == null) {
            tracker = TranslateApiUsageTracker.builder()
                .userId(userId)
                .requestDate(LocalDate.now())
                .build();
            translateApiUsageTrackerRepository.save(tracker);
        }

        // 2. 번역 횟수가 20회가 넘으면 예외 발생시키기
        if (tracker.getRequestCount() >= 20) {
            throw new GeneralException(Code.TRANSLATION_LIMIT_EXCEEDED);
        }

        // 3. 번역하기
        TranslateTextResponse translateTextResponse = translate(text);

        // 4. 번역 횟수 증가시키기
        tracker.setRequestCount(tracker.getRequestCount() + 1);

        return translateTextResponse;
    }


}