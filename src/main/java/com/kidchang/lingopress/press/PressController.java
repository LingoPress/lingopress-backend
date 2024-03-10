package com.kidchang.lingopress.press;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress._base.response.SliceResponseDto;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineRequest;
import com.kidchang.lingopress.press.dto.response.PressContentLineResponse;
import com.kidchang.lingopress.press.dto.response.PressContentResponse;
import com.kidchang.lingopress.press.dto.response.PressResponse;
import com.kidchang.lingopress.press.service.LearnedPressContentLineService;
import com.kidchang.lingopress.press.service.PressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/press")
@RequiredArgsConstructor
public class PressController {

    private final PressService pressService;
    private final LearnedPressContentLineService learnedPressContentLineService;

    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    @Operation(summary = "프레스 전체 리스트 조회")
    @GetMapping("")
    public DataResponseDto<SliceResponseDto<PressResponse>> getPressList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return DataResponseDto.of(
            SliceResponseDto.from(pressService.getPressList(pageable)));
    }

    @Operation(summary = "프레스 상세 조회")
    @GetMapping("/{pressId}")
    public DataResponseDto<PressContentResponse> getPressDetail(
        @PathVariable String pressId) {
        return DataResponseDto.of(pressService.getPressDetail(Long.parseLong(pressId)));
    }

    @Operation(summary = "프레스 한 줄 번역 정답 유무 전송")
    @PostMapping("/translate")
    public DataResponseDto<PressContentLineResponse> checkPressContentLine(
        @Valid @RequestBody TranslateContentLineRequest request) {
        return DataResponseDto.of(learnedPressContentLineService.checkPressContentLine(request));
    }

}
