package com.kidchang.lingopress.apiUsage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ApiUsageTrackerRepository extends
        JpaRepository<ApiUsageTracker, Integer> {

    ApiUsageTracker findByUserIdAndRequestDate(Long userId, LocalDate requestDate);
}
