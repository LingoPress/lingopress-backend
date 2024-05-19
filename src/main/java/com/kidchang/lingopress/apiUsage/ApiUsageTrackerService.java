package com.kidchang.lingopress.apiUsage;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApiUsageTrackerService {
    private final ApiUsageTrackerRepository apiUsageTrackerRepository;

    @Transactional
    public ApiUsageTracker createOrUpdateApiUsageTracker(Long userId, ApiUsageEnum apiUsageEnum) {
        ApiUsageTracker tracker = apiUsageTrackerRepository.findByUserIdAndRequestDate(userId, LocalDate.now());

        tracker = getApiUsageTracker(userId, tracker);

        switch (apiUsageEnum) {
            case SIMILARITY:
                tracker = updateSimilarityApiCount(tracker);
                break;
            case TRANSLATION:
                tracker = updateTranslationApiCount(tracker);
                break;
        }

        return tracker;
    }

    private ApiUsageTracker getApiUsageTracker(Long userId, ApiUsageTracker tracker) {
        if (tracker == null) {
            tracker = ApiUsageTracker.builder()
                    .userId(userId)
                    .requestDate(LocalDate.now())
                    .build();
            apiUsageTrackerRepository.save(tracker);
        }
        return tracker;
    }

    private ApiUsageTracker updateTranslationApiCount(ApiUsageTracker tracker) {
        // 2. 번역 횟수가 20회가 넘으면 예외 발생시키기
        if (tracker.getRequestCount() >= 50) {
            throw new BusinessException(Code.TRANSLATION_LIMIT_EXCEEDED);
        }
        // 4. 번역 횟수 증가시키기
        tracker.setRequestCount(tracker.getRequestCount() + 1);
        return tracker;
    }

    private ApiUsageTracker updateSimilarityApiCount(ApiUsageTracker tracker) {
        int similarityApiCount = tracker.getSimilarityApiCount();
        if (similarityApiCount >= 50) {
            throw new BusinessException(Code.SIMILARITY_LIMIT_EXCEEDED);
        }
        tracker.setSimilarityApiCount(similarityApiCount + 1);
        return tracker;
    }

}
