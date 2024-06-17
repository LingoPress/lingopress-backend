package com.kidchang.lingopress.press;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress._base.response.SliceResponseDto;
import com.kidchang.lingopress.client.LambdaWarmUpClient;
import com.kidchang.lingopress.press.dto.request.TextSimilarityAnalysisRequest;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineMemoRequest;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineRequest;
import com.kidchang.lingopress.press.dto.response.*;
import com.kidchang.lingopress.press.service.LearnPressService;
import com.kidchang.lingopress.press.service.LearnedPressContentLineService;
import com.kidchang.lingopress.press.service.PressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/press")
@RequiredArgsConstructor
public class PressController {

    private final PressService pressService;
    private final LearnPressService learnPressService;
    private final LearnedPressContentLineService learnedPressContentLineService;
    private final LambdaWarmUpClient lambdaWarmUpClient;


    @Operation(summary = "프레스 전체 리스트 조회")
    @GetMapping("")
    public DataResponseDto<SliceResponseDto<PressResponse>> getPressList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "publishedAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            HttpServletRequest request) {
        Pageable pageable;

        if (order.equals("asc")) pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        else pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        String acceptLanguage = request.getHeader("Accept-Language");
        String primaryLanguageCode = "en"; // Default language

        if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
            String[] languages = acceptLanguage.toLowerCase().split(",");
            if (languages.length > 0) {
                String primaryLanguage = languages[0].split(";")[0]; // Extract primary language
                primaryLanguageCode = primaryLanguage.split("-")[0]; // Normalize to language code only (e.g., "ko-kr" to "ko")
            }
        }

        LanguageEnum primaryLanguage;
        try {
            primaryLanguage = LanguageEnum.valueOf(primaryLanguageCode);
        } catch (IllegalArgumentException e) {
            primaryLanguage = LanguageEnum.en; // Default language
        }


        return DataResponseDto.of(
                SliceResponseDto.from(pressService.getPressList(pageable, primaryLanguage)));
    }

    @Operation(summary = "프레스 상세 조회")
    @GetMapping("/{pressId}")
    public DataResponseDto<PressContentResponse> getPressDetail(
            @PathVariable String pressId) {
        return DataResponseDto.of(pressService.getPressDetail(Long.parseLong(pressId)));
    }

    @Operation(summary = "문장 유사도 분석")
    @PostMapping("/similarity")
    public DataResponseDto<TextSimilarityAnalysisResponse> checkPressContentLineSimilarity(

            @RequestBody TextSimilarityAnalysisRequest request) {
        return DataResponseDto.of(learnedPressContentLineService.checkPressContentLineSimilarity(request));
    }

    @Operation(summary = "프레스 한 줄 번역 정답 유무 전송")
    @PostMapping("/translate")
    public DataResponseDto<PressContentLineResponse> checkPressContentLine(
            @Valid @RequestBody TranslateContentLineRequest request) {
        return DataResponseDto.of(learnedPressContentLineService.checkPressContentLine(request));
    }

    @Operation(summary = "프레스 한 줄 메모 저장")
    @PostMapping("/memo")
    public DataResponseDto<PressContentLineResponse> writePressContentLineMemo(
            @Valid @RequestBody TranslateContentLineMemoRequest request) {
        return DataResponseDto.of(learnedPressContentLineService.writePressContentLineMemo(request));
    }

    @Operation(summary = "내 메모 모아보기")
    @GetMapping("/memo")
    public DataResponseDto<SliceResponseDto<PressContentLineResponse>> getMemoList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return DataResponseDto.of(
                SliceResponseDto.from(learnedPressContentLineService.getMemoList(pageable)));
    }

    @Operation(summary = "내가 번역한 프레스 리스트 조회")
    @GetMapping("/learned")
    public DataResponseDto<SliceResponseDto<LearnedPressResponse>> getLearnedPressList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return DataResponseDto.of(
                SliceResponseDto.from(learnPressService.getLearnedPressList(pageable)));
    }

    @Operation(summary = "람다 워밍업")
    @GetMapping("/warming-up")
    public DataResponseDto<String> warmUp() {
        lambdaWarmUpClient.warmUp();
        return DataResponseDto.of("Warming up");
    }

}
