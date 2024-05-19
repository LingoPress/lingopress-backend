package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress._base.constant.CategoryEnum;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.press.entity.Press;
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
        String publishedAt,
        String translatedTitle,
        String author,
        String publisher,
        LanguageEnum language,
        CategoryEnum category
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
                .translatedTitle(press.getTranslatedTitle())
                .author(press.getAuthor())
                .publisher(press.getPublisher())
                .language(press.getLanguage())
                .category(press.getCategory())
                .build();
    }
}
