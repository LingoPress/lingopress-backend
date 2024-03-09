package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineRequest;
import com.kidchang.lingopress.press.dto.response.PressContentResponse;
import com.kidchang.lingopress.press.dto.response.PressResponse;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.entity.PressContent;
import com.kidchang.lingopress.press.repository.LearnedPressContentLineRepository;
import com.kidchang.lingopress.press.repository.PressContentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PressService {

    private final PressRepository pressRepository;
    private final PressContentRepository pressContentRepository;
    private final LearnedPressContentLineRepository learnedPressContentLineRepository;
    private final PressContentService pressContentService;

    public Slice<PressResponse> getPressList(Pageable pageable) {
        Slice<Press> pressSlice = pressRepository.findAll(pageable);
        Slice<PressResponse> pressResponse = pressSlice.map(PressResponse::from);
        return pressResponse;
    }

    public PressContentResponse getPressDetail(Long pressId) {
        Press press = pressRepository.findById(pressId)
            .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));
        List<PressContent> pressContentList = pressContentRepository.getAllByPressId(press.getId());

        String[] originalContentList = pressContentList.stream().map(PressContent::getLineContent)
            .toArray(String[]::new);
        String[] translatedContentList = pressContentList.stream()
            .map(PressContent::getTranslatedLineContent).toArray(String[]::new);

        return PressContentResponse.from(press, originalContentList, translatedContentList);
    }

    public String checkPressContentLine(TranslateContentLineRequest request) {
        // LeanedPress에 있는지 확인하고 없으면 추가

        return null;
    }
}
