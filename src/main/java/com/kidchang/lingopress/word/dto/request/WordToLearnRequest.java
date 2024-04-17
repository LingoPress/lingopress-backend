package com.kidchang.lingopress.word.dto.request;

public record WordToLearnRequest(
        String word,
        String originalText,
        String translatedText,
        Integer lineNumber,
        Long pressId
) {

}
