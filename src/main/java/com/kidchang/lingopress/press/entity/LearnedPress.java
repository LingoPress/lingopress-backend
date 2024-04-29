package com.kidchang.lingopress.press.entity;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import com.kidchang.lingopress.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "옳게 번역한 라인 수")
    private Integer learnedContentLine;
    @Schema(description = "번역한 라인 수. 올바른지 여부 X")
    private Integer translatedContentLine;

    @Builder
    public LearnedPress(User user, Press press) {
        this.user = user;
        this.press = press;
        this.isLearned = false;
        this.comment = "";
        this.learnedContentLine = 0;
        this.translatedContentLine = 0;
    }

    public void increaseLearnedContentLineCount() {
        this.learnedContentLine++;
    }

    public void decreaseLearnedContentLineCount() {
        this.learnedContentLine--;
    }

    public void increaseTranslatedContentLineCount() {
        this.translatedContentLine++;
    }

}
