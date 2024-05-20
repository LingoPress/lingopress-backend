package com.kidchang.lingopress.press.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kidchang.lingopress._base.constant.CategoryEnum;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.press.entity.Press;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PressContentResponse(
        Long id,
        String title,
        String translatedTitle,
        String author,
        String publisher,
        String imageUrl,
        LanguageEnum language,
        String originalUrl,
        Integer totalContentLine,
        Float rating,
        String publishedAt,
        List<PressContentLineResponse> content,
        CategoryEnum category
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
                .author(press.getAuthor())
                .publisher(press.getPublisher())
                .language(press.getLanguage())
                .category(press.getCategory())
                .build();
    }

    public static PressContentResponse from(PressResponse press,
                                            List<PressContentLineResponse> pressContentLines) {
        return PressContentResponse.builder()
                .id(press.id())
                .translatedTitle(press.translatedTitle())
                .title(press.title())
                .imageUrl(press.imageUrl())
                .originalUrl(press.originalUrl())
                .totalContentLine(press.totalContentLine())
                .rating(press.rating())
                .publishedAt(press.publishedAt().toString())
                .content(pressContentLines)
                .author(press.author())
                .publisher(press.publisher())
                .language(press.language())
                .category(press.category())
                .build();
    }
}
