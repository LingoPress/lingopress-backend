package com.kidchang.lingopress.learningRecord.dto;

import java.time.LocalDate;

public record LearningRecordBetweenRequest(
        LocalDate startDate,
        LocalDate endDate
) {
}
