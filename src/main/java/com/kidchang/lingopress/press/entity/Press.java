package com.kidchang.lingopress.press.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
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
    private String language;
    private String category;
    @Schema(description = "저자")
    private String author;
    @Schema(description = "출판사")
    private String publisher;
    @Schema(description = "공개여부")
    private String accessLevel;
}
