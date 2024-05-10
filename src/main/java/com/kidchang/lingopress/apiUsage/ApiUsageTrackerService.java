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
    public ApiUsageTracker createOrUpdateApiUsageTracker(Long userId) {
        ApiUsageTracker tracker = apiUsageTrackerRepository.findByUserIdAndRequestDate(userId, LocalDate.now());

        if (tracker == null) {
            tracker = ApiUsageTracker.builder()
                    .userId(userId)
                    .requestDate(LocalDate.now())
                    .build();
            apiUsageTrackerRepository.save(tracker);
        }

        int similarityApiCount = tracker.getSimilarityApiCount();
        if (similarityApiCount >= 50) {
            throw new BusinessException(Code.SIMILARITY_LIMIT_EXCEEDED);
        }
        tracker.setSimilarityApiCount(similarityApiCount + 1);

        return tracker;
    }
}
