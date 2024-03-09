package com.kidchang.lingopress.press.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class PressContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pressId;
    private Integer lineNumber;
    @Column(columnDefinition = "TEXT")
    private String lineContent;
    @Column(columnDefinition = "TEXT")
    private String translatedLineContent;

}