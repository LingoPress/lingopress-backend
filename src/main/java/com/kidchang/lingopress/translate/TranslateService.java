package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.exception.GlobalException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.apiUsage.ApiUsageTracker;
import com.kidchang.lingopress.apiUsage.ApiUsageTrackerRepository;
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
    private final ApiUsageTrackerRepository apiUsageTrackerRepository;

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

    @Transactional
    public TranslateTextResponse translateWithUsageTracker(LingoGptRequest text) {

        Long userId = SecurityUtil.getUserId();
        // 1. 번역 횟수 기록하기
        ApiUsageTracker tracker = apiUsageTrackerRepository.findByUserIdAndRequestDate(
                userId, LocalDate.now());

        if (tracker == null) {
            tracker = ApiUsageTracker.builder()
                    .userId(userId)
                    .requestDate(LocalDate.now())
                    .build();
            apiUsageTrackerRepository.save(tracker);
        }

        // 2. 번역 횟수가 20회가 넘으면 예외 발생시키기
        if (tracker.getRequestCount() >= 50) {
            throw new BusinessException(Code.TRANSLATION_LIMIT_EXCEEDED);
        }

        // 글자수가 너무 길면 예외 발생시키기
        if (text.getOriginal_text().length() > 1200) {
            throw new GlobalException(Code.TRANSLATION_TEXT_TOO_LONG);
        }

        // 3. 번역하기
        TranslateTextResponse translateTextResponse = translate(text);

        // 4. 번역 횟수 증가시키기
        tracker.setRequestCount(tracker.getRequestCount() + 1);

        return translateTextResponse;
    }


}