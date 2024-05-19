package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PressTranslationContentLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Press press;
    private Integer lineNumber;
    @Column(columnDefinition = "TEXT")
    private String translatedLineContent;
    @Enumerated(EnumType.STRING)
    private LanguageEnum translatedLanguage;


}
