package com.kidchang.lingopress.apiUsage;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "requestDate"})})
public class ApiUsageTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int requestCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int similarityApiCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int videoTranscriptionApiCount;

    @Builder
    public ApiUsageTracker(Long userId, LocalDate requestDate, int requestCount) {
        this.userId = userId;
        this.requestDate = requestDate;
        this.requestCount = requestCount;
    }

}