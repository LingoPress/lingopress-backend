package com.kidchang.lingopress.learningRecord.dto;

import java.time.LocalDate;

public record LearningRecordResponse(
        long userId,
        int learningCount,
        LocalDate date
) {
    public static LearningRecordResponse from(long userId, int learningCount, LocalDate date) {

        return new LearningRecordResponse(userId, learningCount, date);
    }

}
