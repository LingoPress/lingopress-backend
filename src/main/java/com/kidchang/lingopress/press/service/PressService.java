package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.response.PressContentResponse;
import com.kidchang.lingopress.press.dto.response.PressResponse;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.entity.PressContentLine;
import com.kidchang.lingopress.press.repository.PressContentLineRepository;
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
    private final PressContentLineRepository pressContentLineRepository;

    public Slice<PressResponse> getPressList(Pageable pageable) {
        Slice<Press> pressSlice = pressRepository.findAll(pageable);
        Slice<PressResponse> pressResponse = pressSlice.map(PressResponse::from);
        return pressResponse;
    }

    public PressContentResponse getPressDetail(Long pressId) {
        Press press = pressRepository.findById(pressId)
            .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));
        List<PressContentLine> pressContentList = pressContentLineRepository.findAllByPressId(
            press.getId());

        String[] originalTextList = pressContentList.stream().map(PressContentLine::getLineText)
            .toArray(String[]::new);
        String[] translatedTextList = pressContentList.stream()
            .map(PressContentLine::getTranslatedLineText).toArray(String[]::new);

        return PressContentResponse.from(press, originalTextList, translatedTextList);
    }


}
