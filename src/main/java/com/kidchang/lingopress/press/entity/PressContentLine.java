package com.kidchang.lingopress.press.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PressContentLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_id")
    private Press press;
    private Integer lineNumber;
    @Column(columnDefinition = "TEXT")
    private String lineText;
    @Column(columnDefinition = "TEXT")
    private String translatedLineText;

    @Builder
    public PressContentLine(Long id, Press press, Integer lineNumber, String lineText, String translatedLineText) {
        this.id = id;
        this.press = press;
        this.lineNumber = lineNumber;
        this.lineText = lineText;
        this.translatedLineText = translatedLineText;
    }

}
