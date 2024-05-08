package com.kidchang.lingopress.learningRecord;

import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordBetweenRequest;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningRecordService {
    private final LearningRecordRepository learningRecordRepository;


    /**
     * 특정 날짜 learningRecord의 학습 기록 조회
     *
     * @return userId, date로 조회한 학습 기록
     */
    public LearningRecordResponse getLearningRecord() {
        // userId, date로 조회
        // 요청한 아이디와 jwt 아이디가 다르면 에러
        LocalDate today = LocalDate.now();
        LearningRecord.LearningRecordId id = new LearningRecord.LearningRecordId(SecurityUtil.getUserId(), today);
        Optional<LearningRecord> record = learningRecordRepository.findLearningRecordById(id);
        if (record.isEmpty()) {
            return LearningRecordResponse.from(id.getUserId(), 0, today);
        }
        LearningRecord learningRecord = record.get();
        return LearningRecordResponse.from(learningRecord.getId().getUserId(), learningRecord.getLearningCount(), learningRecord.getId().getDate());
    }

    public List<LearningRecordResponse> getLearningRecords(LearningRecordBetweenRequest learningRecordRequest) {
        // userId, date로 조회
        // 요청한 아이디와 jwt 아이디가 다르면 에러
        Long userId = SecurityUtil.getUserId();
        List<LearningRecord> records = learningRecordRepository.findLearningRecordByIdAndDateBetween(userId, learningRecordRequest.startDate(), learningRecordRequest.endDate());
        return records.stream()
                .map(record -> LearningRecordResponse.from(record.getId().getUserId(), record.getLearningCount(), record.getId().getDate()))
                .collect(Collectors.toList());
    }

    /**
     * learningRecord에 학습 기록 저장 & 업데이트
     * 해석이 맞든 틀리든 카운트
     */
    public LearningRecord increaseLearningRecord(long userId, LocalDate date) {
        // 오늘자 기록이 없으면, 새로 생성
        // 오늘자 기록이 있으면, 업데이트
        LearningRecord.LearningRecordId id = new LearningRecord.LearningRecordId(userId, date);
        LearningRecord learningRecord = learningRecordRepository.findLearningRecordById(id)
                .orElseGet(() -> {
                    LearningRecord newLearningRecord = LearningRecord.builder()
                            .userId(userId)
                            .date(date)
                            .build();
                    return learningRecordRepository.save(newLearningRecord);
                });

        learningRecord.increaseLearningCount();
        return learningRecordRepository.save(learningRecord);
    }

}
