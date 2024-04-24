package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import com.kidchang.lingopress.user.User;
import jakarta.persistence.*;
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
    @JoinColumn(name = "learned_press_id")
    private LearnedPress learnedPress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_content_line_id")
    private PressContentLine pressContentLine;
    private Integer lineNumber;
    private String userTranslatedLine;
    private Boolean isCorrect;

    // 문장별메모 기능
    private String memo;

    @Builder
    public LearnedPressContentLine(User user, LearnedPress learnedPress,
                                   PressContentLine pressContentLine,
                                   String userTranslatedLine, Boolean isCorrect, Integer lineNumber, Press press, String memo) {
        this.user = user;
        this.learnedPress = learnedPress;
        this.pressContentLine = pressContentLine;
        this.userTranslatedLine = userTranslatedLine;
        this.isCorrect = isCorrect;
        this.lineNumber = lineNumber;
        this.press = press;
        this.memo = memo;
    }
}
