package com.kidchang.lingopress.learningRecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LearningRecordResponse(
        long userId,
        int count,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate date
) {
    public static LearningRecordResponse from(long userId, int learningCount, LocalDate date) {

        return new LearningRecordResponse(userId, learningCount, date);
    }

}
