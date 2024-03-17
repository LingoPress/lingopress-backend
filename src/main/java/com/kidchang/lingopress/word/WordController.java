package com.kidchang.lingopress.word;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.word.dto.request.WordToLearnRequest;
import com.kidchang.lingopress.word.dto.response.WordToLearnResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
