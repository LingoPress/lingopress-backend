package com.kidchang.lingopress.word;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress._base.utils.LanguageEnumConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WordToLearn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne
    //private User user;
    //@ManyToOne
    //private Press press;
    private Long userId;
    private Long pressId;
    private String word;
    private Integer lineNumber;
    @Column(columnDefinition = "TEXT")
    private String originalLineText;
    private String translatedWord;
    // Boolean은 null이 가능하므로 boolean을 사용.
    @Column(columnDefinition = "boolean default false")
    private boolean isLearned;
    @Convert(converter = LanguageEnumConverter.class)
    private LanguageEnum language;

    @Builder
    public WordToLearn(
            // User user, Press press,
            Long userId, Long pressId, String word, String originalLineText,
            Integer lineNumber, String translatedWord, LanguageEnum language) {
        // this.user = user;
        // this.press = press;
        this.userId = userId;
        this.pressId = pressId;
        this.word = word;
        this.originalLineText = originalLineText;
        this.lineNumber = lineNumber;
        this.translatedWord = translatedWord;
        this.language = language;
    }

    public void updateTranslatedWord(String word) {
        this.translatedWord = word;
    }
}
