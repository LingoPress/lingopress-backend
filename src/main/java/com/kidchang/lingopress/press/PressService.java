package com.kidchang.lingopress.press;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.press.dto.response.PressContentResponse;
import com.kidchang.lingopress.press.dto.response.PressResponse;
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
}
