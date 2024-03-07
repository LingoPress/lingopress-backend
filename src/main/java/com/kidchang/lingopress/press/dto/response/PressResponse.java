package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.press.Press;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressResponse(
    Long id,
    String title,
    String content,
    String imageUrl,
    String originalUrl,
    Integer totalContentLine,
    Float rating,
    String publishedAt
) {

    public static PressResponse from(Press press) {
        return PressResponse.builder()
            .id(press.getId())
            .title(press.getTitle())
            .content(press.getContent())
            .imageUrl(press.getImageUrl())
            .originalUrl(press.getOriginalUrl())
            .totalContentLine(press.getTotalContentLine())
            .rating(press.getRating())
            .publishedAt(press.getPublishedAt().toString())
            .build();
    }
}
