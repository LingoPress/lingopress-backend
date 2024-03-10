package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import com.kidchang.lingopress.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class LearnedPressContentLine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //        pressId가 필요한지 고민해봐야함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_id")
    private Press press;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaned_press_id")
    private LearnedPress learnedPress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_content_line_id")
    private PressContentLine pressContent;
    private Integer pressContentLineNumber;
    private String userTranslatedLine;
    private Boolean isCorrect;

    @Builder
    public LearnedPressContentLine(User user, LearnedPress learnedPress,
        PressContentLine pressContent,
        String userTranslatedLine, Boolean isCorrect, Integer pressContentLineNumber, Press press) {
        this.user = user;
        this.learnedPress = learnedPress;
        this.pressContent = pressContent;
        this.userTranslatedLine = userTranslatedLine;
        this.isCorrect = isCorrect;
        this.pressContentLineNumber = pressContentLineNumber;
        this.press = press;
    }
}
