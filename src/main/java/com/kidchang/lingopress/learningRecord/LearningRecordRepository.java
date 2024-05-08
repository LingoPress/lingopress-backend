package com.kidchang.lingopress.learningRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> {

    Optional<LearningRecord> findLearningRecordById(LearningRecord.LearningRecordId id);

    // List<LearningRecord> findLearningRecordByIdBetween(LearningRecord.LearningRecordId id, LearningRecord.LearningRecordId id2);

    @Query("select lr from LearningRecord lr where lr.id.userId = :userId and lr.id.date between :startDate and :endDate")
    List<LearningRecord> findLearningRecordByIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
