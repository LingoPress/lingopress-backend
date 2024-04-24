package com.kidchang.lingopress.learningRecord.dto;

import java.time.LocalDate;

public record LearningRecordRequest(
        LocalDate date
) {
}
