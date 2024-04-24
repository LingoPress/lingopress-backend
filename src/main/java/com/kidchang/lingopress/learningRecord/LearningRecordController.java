package com.kidchang.lingopress.learningRecord;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordBetweenRequest;
import com.kidchang.lingopress.learningRecord.dto.LearningRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/learning-record")
@RequiredArgsConstructor
public class LearningRecordController {
    private final LearningRecordService learningRecordService;

    @Operation(summary = "당일 학습 기록 조회")
    @GetMapping("/today")
    public DataResponseDto<LearningRecordResponse> getLearningRecord(
    ) {
        return DataResponseDto.of(learningRecordService.getLearningRecord());
    }

    @Operation(summary = "특정 기간 학습 기록 조회")
    @GetMapping("/between")
    public DataResponseDto<List<LearningRecordResponse>> getLearningRecords(
            LearningRecordBetweenRequest learningRecordRequest
    ) {
        return DataResponseDto.of(learningRecordService.getLearningRecords(learningRecordRequest));
    }
}
