package com.kidchang.lingopress.press.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
public class Press {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String originalUrl;
    private String imageUrl;
    private Integer totalContentLine;
    @ColumnDefault("3")
    @Size(min = 0, max = 5, message = "유저 점수의 평균은 0~5사이 입니다.")
    private Float rating;
    private Date publishedAt;
}
