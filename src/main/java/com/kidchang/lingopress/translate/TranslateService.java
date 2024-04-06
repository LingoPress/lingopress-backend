package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.client.LingoGptClient;
import com.kidchang.lingopress.translate.dto.request.LingoGptRequest;
import com.kidchang.lingopress.translate.dto.response.LingoGptResponse;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateService {

    private final LingoGptClient lingoGptClient;
    private final TranslateApiUsageTrackerRepository translateApiUsageTrackerRepository;

    public TranslateTextResponse translate(LingoGptRequest text) {

        try {
            LingoGptResponse lingoGptResponse = lingoGptClient.translate(text);
            log.info("@@ using token : {}", lingoGptResponse.getToken());
            return TranslateTextResponse.builder()
                    .translatedText(lingoGptResponse.getText())
                    .build();

        } catch (Exception e) {
            throw new GeneralException(Code.TRANSLATION_ERROR, e);
        }
    }

    @Transactional
    public TranslateTextResponse translateWithUsageTracker(LingoGptRequest text) {

        Long userId = SecurityUtil.getUserId();
        // 1. 번역 횟수 기록하기
        TranslateApiUsageTracker tracker = translateApiUsageTrackerRepository.findByUserIdAndRequestDate(
                userId, LocalDate.now());

        if (tracker == null) {
            tracker = TranslateApiUsageTracker.builder()
                    .userId(userId)
                    .requestDate(LocalDate.now())
                    .build();
            translateApiUsageTrackerRepository.save(tracker);
        }

        // 2. 번역 횟수가 20회가 넘으면 예외 발생시키기
        if (tracker.getRequestCount() >= 50) {
            throw new GeneralException(Code.TRANSLATION_LIMIT_EXCEEDED);
        }

        // 3. 번역하기
        TranslateTextResponse translateTextResponse = translate(text);

        // 4. 번역 횟수 증가시키기
        tracker.setRequestCount(tracker.getRequestCount() + 1);

        return translateTextResponse;
    }


}