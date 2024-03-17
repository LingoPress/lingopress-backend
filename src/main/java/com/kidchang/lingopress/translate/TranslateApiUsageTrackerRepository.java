package com.kidchang.lingopress.translate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslateApiUsageTrackerRepository extends
    JpaRepository<TranslateApiUsageTracker, Integer> {

    TranslateApiUsageTracker findByUserId(Long userId);

}
