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
        int limit = switch (apiUsageEnum) {
            case SIMILARITY, TRANSLATION -> 50;
            case VIDEO_TRANSCRIPTION -> 5;
        };

        return updateApiCount(getApiUsageTracker(userId, tracker), apiUsageEnum, limit);
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

    private ApiUsageTracker updateApiCount(ApiUsageTracker tracker, ApiUsageEnum apiUsageEnum, int limit) {
        int apiCount = switch (apiUsageEnum) {
            case SIMILARITY -> tracker.getSimilarityApiCount();
            case TRANSLATION -> tracker.getRequestCount();
            case VIDEO_TRANSCRIPTION -> tracker.getVideoTranscriptionApiCount();
        };
        if (apiCount >= limit) {
            throw new BusinessException(
                    switch (apiUsageEnum) {
                        case SIMILARITY -> Code.SIMILARITY_LIMIT_EXCEEDED;
                        case TRANSLATION -> Code.TRANSLATION_LIMIT_EXCEEDED;
                        case VIDEO_TRANSCRIPTION -> Code.VIDEO_TRANSCRIPTION_LIMIT_EXCEEDED;
                    }
            );
        }
        // 번역 횟수 증가시키기
        switch (apiUsageEnum) {
            case SIMILARITY -> tracker.setSimilarityApiCount(apiCount + 1);
            case TRANSLATION -> tracker.setRequestCount(apiCount + 1);
            case VIDEO_TRANSCRIPTION -> tracker.setVideoTranscriptionApiCount(apiCount + 1);
        }
        return tracker;
    }

}
