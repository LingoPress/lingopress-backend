package com.kidchang.lingopress.press.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PressContentLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pressId;
    private Integer lineNumber;
    @Column(columnDefinition = "TEXT")
    private String lineText;
    @Column(columnDefinition = "TEXT")
    private String translatedLineText;

}
