package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PressTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Press press;
    private String translatedTitle;
    @Enumerated(EnumType.STRING)
    private LanguageEnum translatedLanguage;
}
