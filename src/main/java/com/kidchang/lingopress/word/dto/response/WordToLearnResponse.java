package com.kidchang.lingopress.word.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.word.WordToLearn;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record WordToLearnResponse(
    Long id,
    String word,
    String translatedWord,
    String originalLineText,
    Integer lineNumber,
    boolean isLearned,
    Long pressId
) {


    public static List<WordToLearnResponse> listOf(List<WordToLearn> wordsToLearn) {
        return wordsToLearn.stream()
            .map(WordToLearnResponse::of)
            .toList();
    }

    private static WordToLearnResponse of(WordToLearn wordToLearn) {
        return WordToLearnResponse.builder()
            .id(wordToLearn.getId())
            .word(wordToLearn.getWord())
            .translatedWord(wordToLearn.getTranslatedWord())
            .originalLineText(wordToLearn.getOriginalLineText())
            .lineNumber(wordToLearn.getLineNumber())
            .isLearned(wordToLearn.isLearned())
            .pressId(wordToLearn.getPressId())
            .build();
    }
}
