package com.kidchang.lingopress.learningRecord;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordRequest;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningRecordService {
    private final LearningRecordRepository learningRecordRepository;


    /**
     * 특정 날짜 learningRecord의 학습 기록 조회
     *
     * @return userId, date로 조회한 학습 기록
     */
    public LearningRecordResponse getLearningRecord(LearningRecordRequest learningRecordRequest) {
        // userId, date로 조회
        // 요청한 아이디와 jwt 아이디가 다르면 에러
        LearningRecord.LearningRecordId id = new LearningRecord.LearningRecordId(SecurityUtil.getUserId(), learningRecordRequest.date());
        LearningRecord record = learningRecordRepository.findLearningRecordById(id).orElseThrow(() -> new BusinessException(Code.NOT_FOUND_LEARNING_RECORD));
        return LearningRecordResponse.from(record.getId().getUserId(), record.getLearningCount(), record.getId().getDate());
    }


}
