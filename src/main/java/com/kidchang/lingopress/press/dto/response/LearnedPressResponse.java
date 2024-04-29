package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.press.entity.LearnedPress;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LearnedPressResponse(
        Long id,
        PressResponse press,
        Boolean isLearned,
        String comment,
        Integer rating,
        Integer learnedContentLine,
        LocalDateTime updatedAt,
        Integer translatedContentLine

) {

    public static LearnedPressResponse from(LearnedPress learnedPress) {
        return LearnedPressResponse.builder()
                .id(learnedPress.getId())
                .press(PressResponse.from(learnedPress.getPress()))
                .isLearned(learnedPress.getIsLearned())
                .comment(learnedPress.getComment())
                .rating(learnedPress.getRating())
                .learnedContentLine(learnedPress.getLearnedContentLine())
                .translatedContentLine(learnedPress.getTranslatedContentLine())
                .updatedAt(learnedPress.getUpdatedAt())
                .build();
    }
}
