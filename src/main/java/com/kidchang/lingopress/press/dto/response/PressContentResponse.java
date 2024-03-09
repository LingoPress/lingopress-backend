package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.press.Press;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressContentResponse(
    Long id,
    String title,
//    String content,
    String imageUrl,
    String originalUrl,
    Integer totalContentLine,
    Float rating,
    String publishedAt,
    String[] originalContent,
    String[] translatedContent

) {

    public static PressContentResponse from(Press press, String[] originalContent,
        String[] translatedContent) {
        return PressContentResponse.builder()
            .id(press.getId())
            .title(press.getTitle())
            .imageUrl(press.getImageUrl())
            .originalUrl(press.getOriginalUrl())
            .totalContentLine(press.getTotalContentLine())
            .rating(press.getRating())
            .publishedAt(press.getPublishedAt().toString())
            .originalContent(originalContent)
            .translatedContent(translatedContent)
            .build();
    }
}