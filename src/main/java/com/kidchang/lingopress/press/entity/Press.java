package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.constant.CategoryEnum;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Press {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String translatedTitle;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String originalUrl;
    private String imageUrl;
    private Integer totalContentLine;
    @Size(min = 0, max = 5, message = "유저 점수의 평균은 0~5사이 입니다.")
    private Float rating;
    private LocalDateTime publishedAt;
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    @Schema(description = "저자")
    private String author;
    @Schema(description = "출판사")
    private String publisher;
    @Schema(description = "공개여부")
    private String accessLevel;
    @Enumerated(EnumType.STRING)
    private LanguageEnum language;
    @Schema(description = "private일 경우 소유자")
    @ManyToOne
    private User owner;

    @Builder
    public Press(Long id, String title, String translatedTitle, String content, String originalUrl, String imageUrl, Integer totalContentLine, Float rating, LocalDateTime publishedAt, LanguageEnum language, CategoryEnum category, String author, String publisher, String accessLevel, User owner) {
        this.id = id;
        this.title = title;
        this.translatedTitle = translatedTitle;
        this.content = content;
        this.originalUrl = originalUrl;
        this.imageUrl = imageUrl;
        this.totalContentLine = totalContentLine;
        this.rating = rating;
        this.publishedAt = publishedAt;
        this.language = language;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.accessLevel = accessLevel;
        this.owner = owner;
    }


}
