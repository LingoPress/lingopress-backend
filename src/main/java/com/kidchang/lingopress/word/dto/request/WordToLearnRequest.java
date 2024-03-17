package com.kidchang.lingopress.word.dto.request;

public record WordToLearnRequest(
    String word,
    String originalText,
    Integer lineNumber,
    Long pressId
) {

}
