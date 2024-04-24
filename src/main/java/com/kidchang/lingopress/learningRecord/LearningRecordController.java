package com.kidchang.lingopress.learningRecord;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordRequest;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/learning-record")
@RequiredArgsConstructor
public class LearningRecordController {
    private final LearningRecordService learningRecordService;

    @Operation(summary = "당일 학습 기록 조회")
    @GetMapping("")
    public DataResponseDto<LearningRecordResponse> getLearningRecord(
            LearningRecordRequest learningRecordRequest
    ) {
        return DataResponseDto.of(learningRecordService.getLearningRecord(learningRecordRequest));
    }
}
