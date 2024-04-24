package com.kidchang.lingopress.learningRecord;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class LearningRecord {
    @EmbeddedId
    private LearningRecordId id;
    //    @Id
//    private long userId;
//
//    @Id
//    private LocalDate date;
    private int learningCount;


    @Builder
    public LearningRecord(long userId, LocalDate date) {
        this.id = new LearningRecordId();
        this.id.userId = userId;
        this.id.date = date;
        this.learningCount = 0;
    }

    public void increaseLearningCount() {
        this.learningCount += 1;
    }

    // 복합키
    @Embeddable
    @NoArgsConstructor
    @Getter
    public static class LearningRecordId implements Serializable {
        private Long userId;
        private LocalDate date;

        @Builder
        public LearningRecordId(Long userId, LocalDate date) {
            this.userId = userId;
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LearningRecordId that = (LearningRecordId) o;
            return userId.equals(that.userId) && date.equals(that.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, date);
        }

    }
}
