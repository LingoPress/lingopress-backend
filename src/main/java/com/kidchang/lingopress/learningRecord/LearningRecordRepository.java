package com.kidchang.lingopress.learningRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> {

    Optional<LearningRecord> findLearningRecordById(LearningRecord.LearningRecordId id);

    List<LearningRecord> findLearningRecordByIdBetween(LearningRecord.LearningRecordId id, LearningRecord.LearningRecordId id2);

}
