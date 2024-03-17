package com.kidchang.lingopress.translate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "requestDate"})})
public class TranslateApiUsageTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int requestCount;

    @Builder
    public TranslateApiUsageTracker(Long userId, LocalDate requestDate, int requestCount) {
        this.userId = userId;
        this.requestDate = requestDate;
        this.requestCount = requestCount;
    }

}