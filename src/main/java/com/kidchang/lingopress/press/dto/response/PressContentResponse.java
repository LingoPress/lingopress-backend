package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress.press.entity.Press;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressContentResponse(
    Long id,
    String title,
    String imageUrl,
    String originalUrl,
    Integer totalContentLine,
    Float rating,
    String publishedAt,
    List<PressContentLineResponse> content
) {

    public static PressContentResponse from(Press press,
        List<PressContentLineResponse> pressContentLines) {
        return PressContentResponse.builder()
            .id(press.getId())
            .title(press.getTitle())
            .imageUrl(press.getImageUrl())
            .originalUrl(press.getOriginalUrl())
            .totalContentLine(press.getTotalContentLine())
            .rating(press.getRating())
            .publishedAt(press.getPublishedAt().toString())
            .content(pressContentLines)
            .build();
    }
}
