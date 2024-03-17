package com.kidchang.lingopress.translate;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.translate.dto.request.TranslateTextRequest;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/translate")
@RequiredArgsConstructor
@Slf4j
public class TranslateController {

    private final TranslateService translateService;

    @Deprecated
    @Operation(summary = "DeepL로 번역")
    @PostMapping("/machine")
    public DataResponseDto<TranslateTextResponse> translateTextInMachine(
        @RequestBody TranslateTextRequest originalText) {

        return DataResponseDto.of(translateService.translate(originalText));
    }
}