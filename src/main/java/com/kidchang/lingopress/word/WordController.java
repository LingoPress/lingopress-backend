package com.kidchang.lingopress.word;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.word.dto.request.WordToLearnRequest;
import com.kidchang.lingopress.word.dto.response.WordToLearnResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @PostMapping("/need-to-learn")
    public DataResponseDto<WordToLearnResponse> enrollWordToLearn(
            @RequestBody WordToLearnRequest request
    ) {
        return DataResponseDto.of(wordService.enrollWordToLearn(request));
    }

    @GetMapping("/need-to-learn/{pressId}")
    public DataResponseDto<List<WordToLearnResponse>> getWordToLearn(
            @PathVariable Long pressId
    ) {
        return DataResponseDto.of(wordService.getWordToLearn(pressId));
    }

    @Operation(summary = "내가 학습할 단어 목록 조회")
    @GetMapping("/need-to-learn")
    public DataResponseDto<List<WordToLearnResponse>> getMyWords() {
        return DataResponseDto.of(wordService.getWordToLearn());
    }

    @Operation(summary = "단어 수정")
    @PutMapping("/{wordId}")
    public DataResponseDto<String> updateWord(
            @PathVariable Long wordId,
            @RequestBody WordToLearnRequest translatedWord
    ) {
        return DataResponseDto.of(wordService.updateWord(wordId, translatedWord));
    }

    @Operation(summary = "단어 삭제")
    @DeleteMapping("/{wordId}")
    public DataResponseDto<String> deleteWord(
            @PathVariable Long wordId
    ) {
        return DataResponseDto.of(wordService.deleteWord(wordId));
    }


}
