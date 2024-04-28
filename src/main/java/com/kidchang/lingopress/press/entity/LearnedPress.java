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
public class LearnedPress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_id")
    private Press press;
    private Boolean isLearned;
    private String comment;
    private Integer rating;
    private Integer learnedContentLine;

    @Builder
    public LearnedPress(User user, Press press) {
        this.user = user;
        this.press = press;
        this.isLearned = false;
        this.comment = "";
        this.learnedContentLine = 0;
    }

    public void increaseTranslatedLineCount() {
        this.learnedContentLine++;
    }

    public void decreaseTranslatedLineCount() {
        this.learnedContentLine--;
    }
}
