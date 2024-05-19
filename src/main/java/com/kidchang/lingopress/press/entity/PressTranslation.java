package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress._base.utils.LanguageEnumConverter;
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
    @Convert(converter = LanguageEnumConverter.class)
    private LanguageEnum translatedLanguage;
}
